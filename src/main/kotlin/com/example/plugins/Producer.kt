package com.example.plugins

import com.example.routes.postMessageRoute
import com.example.routes.postUserRoute
import com.rabbitmq.client.ConnectionFactory
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.producerRouting() {
    val logger = log

    routing {
        route("/produce") {

            postUserRoute(logger)

            postMessageRoute(logger)

        }
    }

}