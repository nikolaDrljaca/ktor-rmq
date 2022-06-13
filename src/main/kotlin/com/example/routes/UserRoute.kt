package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*

data class User(
    val name: String,
    val email: String
)

data class UserEvent(
    val name: String,
    val email: String,
    val tag: String = "user"
)

private fun User.convertToEvent() = UserEvent(
    name = this.name,
    email = this.email
)

fun Route.postUserRoute(
    log: Logger
) {
    post("/user") {
        val user = call.receive<User>()
        log.info("User request received: $user")

        call.respond(user.convertToEvent())
    }
}