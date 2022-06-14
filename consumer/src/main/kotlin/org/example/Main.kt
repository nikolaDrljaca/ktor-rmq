package org.example

import com.rabbitmq.client.ConnectionFactory
import org.example.dao.DatabaseFactory
import org.example.dao.messageDao
import org.example.dao.userDao

suspend fun main() {
    DatabaseFactory.init()
    val factory = ConnectionFactory().apply {
//        username = "guest"
//        password = "guest"
//        host = "rabbit-1"
//        port = 5672
    }

    println("Factory created!")

    //don't close this in the consumer, keep it running to receive messages
    val connection = factory.newConnection()
    println("Established connection!")

    val channel = connection.createChannel()
    channel.queueDeclare(
        "hello_world", //queue name
        false, //durable
        false, //exclusive
        false, //autoDelete
        null //arguments
    )
    println("Channel created!")

    channel.basicConsumeDeliveryFlow("hello_world", true)
        .collect { delivery ->
            when(delivery) {
                is ChannelHelper.Delivery -> {
                    println("Received Message!")

                    decodeJsonDelivery(delivery.delivery.body.decodeToString())?.let {
                        when(it) {
                            is BaseDelivery.MessageDelivery -> {
                                messageDao.insert(userId = it.userId, content = it.content)
                            }
                            is BaseDelivery.UserDelivery -> {
                                userDao.insert(name = it.name, email = it.email)
                            }
                        }
                    }

                    println("---------------------------")
                    println(messageDao.getAllMessages().joinToString(separator = "\n", limit = 20))
                }
                is ChannelHelper.Cancel -> {
                    //Do nothing
                }
            }
        }
}

