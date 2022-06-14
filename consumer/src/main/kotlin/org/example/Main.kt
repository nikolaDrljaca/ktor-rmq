package org.example

import org.example.dao.DatabaseFactory
import org.example.dao.messageDao
import org.example.dao.userDao

suspend fun main() {
    DatabaseFactory.init()

    val e1 = userDao.insert("NikolaD", email = "nikola@doa.com")
    val e2 = userDao.insert("joe_rogan", email = "joe@rogan.com")
    val e3 = userDao.insert("mike", email = "mike@you.com")
    val e4 = userDao.insert("arnold", email = "arnold@doa.org")

    messageDao.apply {
        insert(e1.id.value, generateRandomString())
        insert(e1.id.value, generateRandomString())
        insert(e1.id.value, generateRandomString())
        insert(e1.id.value, generateRandomString())

        insert(e2.id.value, generateRandomString())
        insert(e2.id.value, generateRandomString())
        insert(e2.id.value, generateRandomString())

        insert(e3.id.value, generateRandomString())
        insert(e3.id.value, generateRandomString())

        insert(e4.id.value, generateRandomString())
        insert(e4.id.value, generateRandomString())

        insert(332, generateRandomString())
    }

    println("----------- Initialization finished. -----------")

    println("User 1:")
    println(e1.messages.toList().joinToString("\n"))
    println("User 2:")
    println(e2.messages.toList().joinToString("\n"))
    println("User 3:")
    println(e3.messages.toList().joinToString("\n"))
    println("User 4:")
    println(e4.messages.toList().joinToString("\n"))

    userDao.deleteUser(e2.id.value)

    println(userDao.getAllUsers().joinToString("\n"))

    println("Messages ==========")
    println(messageDao.getAllMessages().joinToString("\n"))

    val message = messageDao.getMessageById(3)
    println(message)

    messageDao.updateMessage(3, "New content")

    messageDao.deleteMessage(2)


    /*
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

     */
}

