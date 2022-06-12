package com.example.plugins

import com.rabbitmq.client.ConnectionFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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

fun Application.producerRouting() {
    val logger = log

    routing {
        route("/produce") {

            post("/{message}") {

                val message = call.parameters["message"] ?: "default message"
                logger.info("Message received: $message")

                val connectionFactory = ConnectionFactory().apply {
                    username = "guest"
                    password = "guest"
                    host = "rabbit-1" //hostname of rabbitmq container, use the same docker network, can be passed in as environment variable
                    port = 5672
                }
                logger.info("Connection factory created.")

                runCatching {
                    //create connection, can throw errors
                    val connection = connectionFactory.newConnection()
                    connection.use {
                        logger.info("Established new connection.")

                        //create a channel, virtual connection that interacts with Queues
                        val channel = connection.createChannel()
                        channel.use {
                            logger.info("Created new channel.")

                            //declare a queue, if it exists nothing happens
                            channel.queueDeclare(
                                "hello_world", //queue name
                                false, //durable
                                false, //exclusive
                                false, //autoDelete
                                null //arguments
                            )
                            logger.info("Declared queue named hello_world.")

                            //publish a message to the queue
                            channel.basicPublish(
                                "", //exchange
                                "hello_world", //routing key
                                false, //mandatory
                                null, //props
                                message.toByteArray() //message to publish as ByteArray
                            )
                            logger.info("Successfully published!")
                            call.respond(HttpStatusCode.OK)
                        }

                    }
                }
                    .onFailure { exception ->
                        logger.error("Caught exception: ", exception)
                    }
            }

        }
    }

}