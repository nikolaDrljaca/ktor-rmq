package com.example.plugins

import com.example.routes.postMessageRoute
import com.example.routes.postUserRoute
import com.rabbitmq.client.Channel
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.producerRouting(
    channel: Channel
) {
    val logger = log

    routing {
        route("/produce") {

            postUserRoute(channel = channel, log = logger)

            postMessageRoute(channel = channel, log = logger)

        }
    }

}