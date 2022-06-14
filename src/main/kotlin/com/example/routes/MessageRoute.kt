package com.example.routes

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
@kotlinx.serialization.Serializable
data class Message(
    val content: String,
    val userId: Int
)

fun Route.postMessageRoute(
    channel: Channel,
    log: Logger
) {
    post("/message") {

        val message = call.receive<Message>()
        log.info("Message received: $message")

        runCatching {
            val properties = BasicProperties.Builder()
                .contentType("application/json")
                .build()

            val payload = Json.encodeToString(message.payload())

            //publish a message to the queue
            channel.basicPublish(
                "", //exchange
                "hello_world", //routing key
                false, //mandatory
                properties, //props
                payload.toByteArray() //message to publish as ByteArray
            )
            log.info("Successfully published!")
            call.respond(HttpStatusCode.OK)

        }
            .onFailure { exception ->
                log.error("Caught exception: ", exception)
                call.respond(HttpStatusCode.BadRequest)
            }
    }
}