package org.example

import org.example.dao.DatabaseFactory
import org.example.dao.messageDao

suspend fun main() {
    DatabaseFactory.init()

    println("Executing SQL statements.")

    val allMessages = messageDao.getAllMessages()
    println("Message table content: $allMessages")

    println("Inserting messages.")
    val message = messageDao.insert("This is the first message")
    messageDao.apply {
        insert("Second after first")
        insert("foo bar")
        insert("bazz bazz")
    }

    println("Message table content after insertion ${messageDao.getAllMessages()}")

    message?.let { messageDao.deleteMessage(it.id) }

    println("Message table content after deletion ${messageDao.getAllMessages()}")

    val foo = messageDao.getAllMessages().first()

    messageDao.updateMessage(foo.id, "This is updated content.")

    /*
    val factory = ConnectionFactory().apply {
        username = "guest"
        password = "guest"
        host = "rabbit-1"
        port = 5672
    }

    println("Factory created!")

    //don't close this in the consumer, keep it running to receive messages
    val connection = factory.newConnection()
    println("Established connection")

    val channel = connection.createChannel()
    channel.queueDeclare(
        "hello_world", //queue name
        false, //durable
        false, //exclusive
        false, //autoDelete
        null //arguments
    )
    println("Channel created")

    channel.basicConsumeDeliveryFlow("hello_world", true)
        .collect {
            when(it) {
                is ChannelHelper.Delivery -> {  }
                is ChannelHelper.Cancel -> {  }
            }
        }


     */

    /*
    val deliveryCallback: (String, Delivery) -> Unit = { consumerTag, delivery ->
        println("Received message: ${delivery.body.decodeToString()}")
    }

    val cancelCallback: (String) -> Unit = {

    }

    channel.basicConsume(
        "hello_world", //queue name
        true, //acknowledgement
        deliveryCallback,
        cancelCallback
    )
     */
}

