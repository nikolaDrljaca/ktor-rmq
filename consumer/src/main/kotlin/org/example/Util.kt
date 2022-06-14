package org.example

import com.rabbitmq.client.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

sealed class ChannelHelper {
    abstract val consumerTag: String

    data class Delivery(override val consumerTag: String, val delivery: com.rabbitmq.client.Delivery) : ChannelHelper()
    data class Cancel(override val consumerTag: String) : ChannelHelper()
}

fun Channel.basicConsumeDeliveryFlow(queueName: String, autoAck: Boolean) = callbackFlow {
    basicConsume(
        queueName,
        autoAck,
        { consumerTag, delivery -> trySend(ChannelHelper.Delivery(consumerTag, delivery)) },
        { consumerTag -> trySend(ChannelHelper.Cancel(consumerTag)) } //called when the consumer is cancelled
    )

    awaitClose { close() }
}

sealed class BaseDelivery {
    @kotlinx.serialization.Serializable
    data class UserDelivery(val name: String, val email: String): BaseDelivery()
    @kotlinx.serialization.Serializable
    data class MessageDelivery(val userId: Int, val content: String): BaseDelivery()
}

fun decodeJsonDelivery(input: String): BaseDelivery? {
    val payload = Json.decodeFromString<Map<String, String>>(input)
    return when (payload["tag"]) {
            "user" -> { Json.decodeFromString<BaseDelivery.UserDelivery>(payload["payload"] ?: "") }
            "msg" -> { Json.decodeFromString<BaseDelivery.MessageDelivery>(payload["payload"] ?: "") }
            else -> { null }
    }
}
