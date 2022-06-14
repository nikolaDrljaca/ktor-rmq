package com.example.routes

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@kotlinx.serialization.Serializable
data class Payload(
    val tag: String,
    val payload: String
)

fun Message.payload() = Payload(
    tag = "msg",
    payload = Json.encodeToString(this)
)

fun User.payload() = Payload(
    tag = "user",
    payload = Json.encodeToString(this)
)
