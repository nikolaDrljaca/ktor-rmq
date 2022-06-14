package com.example.rmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory

object RmqFactory {
    //private val host = System.getenv("HOSTNAME")

    private val connectionFactory = ConnectionFactory().apply {
        //username = "guest"
        //password = "guest"
        //host = "rabbit-1"
        //port = 5672
    }

    fun init(
        queueName: String = "hello_world",
        durable: Boolean = false,
        exclusive: Boolean = false,
        autoDelete: Boolean = false,
        args: Map<String, Any>? = null
    ): Channel {
        val connection = connectionFactory.newConnection()
        val channel = connection.createChannel()

        //declare a queue, if it exists nothing happens
        channel.queueDeclare(
            queueName, //queue name
            durable, //durable
            exclusive, //exclusive
            autoDelete, //autoDelete
            args //arguments
        )
        return channel
    }


}