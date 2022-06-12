package org.example

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery

fun main() {

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

}

