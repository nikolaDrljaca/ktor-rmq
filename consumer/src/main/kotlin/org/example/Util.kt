package org.example

import com.rabbitmq.client.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

sealed class ChannelHelper {
    abstract val consumerTag: String
    data class Delivery(override val consumerTag: String, val delivery: com.rabbitmq.client.Delivery): ChannelHelper()
    data class Cancel(override val consumerTag: String): ChannelHelper()
}

fun Channel.basicConsumeDeliveryFlow(queueName: String, autoAck: Boolean) = callbackFlow {
    basicConsume(
        queueName,
        autoAck,
        { consumerTag, delivery -> trySend(ChannelHelper.Delivery(consumerTag, delivery)) },
        { consumerTag ->  trySend(ChannelHelper.Cancel(consumerTag))} //called when the consumer is cancelled
    )

    awaitClose { close() }
}
