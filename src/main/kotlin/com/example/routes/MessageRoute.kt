package com.example.routes

import com.rabbitmq.client.ConnectionFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*

/**
 * 1. We need a connection to the rabbitmq client, configure it - Use [ConnectionFactory]
 * 2. Create a new connection using the factory - use `factory.newConnection()`
 * 3. Create a channel, this is a virtual connection to the queue
 * 4. Declare queue, this will create the queue if it does not exist
 * 5. Publish messages.
 *
 *
 * docker command:
 * docker run --rm --net <network_name> -e <environment_variables: NAME=VALUE> -p 80:80 <image_name>
 */
fun Route.postMessageRoute(
    log: Logger
) {
    post("/{message}") {

        val message = call.parameters["message"] ?: "default message"
        log.info("Message received: $message")

        val connectionFactory = ConnectionFactory().apply {
            username = "guest"
            password = "guest"
            host = "rabbit-1" //hostname of rabbitmq container, use the same docker network, can be passed in as environment variable
            port = 5672
        }
        log.info("Connection factory created.")

        runCatching {
            //create connection, can throw errors
            val connection = connectionFactory.newConnection()
            connection.use {
                log.info("Established new connection.")

                //create a channel, virtual connection that interacts with Queues
                val channel = connection.createChannel()
                channel.use {
                    log.info("Created new channel.")

                    //declare a queue, if it exists nothing happens
                    channel.queueDeclare(
                        "hello_world", //queue name
                        false, //durable
                        false, //exclusive
                        false, //autoDelete
                        null //arguments
                    )
                    log.info("Declared queue named hello_world.")

                    //publish a message to the queue
                    channel.basicPublish(
                        "", //exchange
                        "hello_world", //routing key
                        false, //mandatory
                        null, //props
                        message.toByteArray() //message to publish as ByteArray
                    )
                    log.info("Successfully published!")
                    call.respond(HttpStatusCode.OK)
                }

            }
        }
            .onFailure { exception ->
                log.error("Caught exception: ", exception)
            }
    }
}