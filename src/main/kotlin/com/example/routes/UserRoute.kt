package com.example.routes

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@kotlinx.serialization.Serializable
data class User(
    val name: String,
    val email: String
)

fun Route.postUserRoute(
    channel: Channel,
    log: Logger
) {
    post("/user") {
        val user = call.receive<User>()
        log.info("User request received: $user")


        runCatching {
            //channel.use {
                val properties = AMQP.BasicProperties.Builder()
                    .contentType("application/json")
                    .build()

                val payload = Json.encodeToString(user)

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
           // }
        }
            .onFailure { exception ->
                log.error("Caught exception: ", exception)
                call.respond(HttpStatusCode.BadRequest)
            }

    }
}