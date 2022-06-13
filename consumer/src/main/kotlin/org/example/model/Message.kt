package org.example.model

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Message(
    val id: Int,
    val content: String,
    private val instantTimestamp: Instant
) {
    val timestamp: String = DateTimeFormatter
        .ofPattern("dd.MM.yyyy HH:mm:ss")
        .withZone(ZoneId.systemDefault())
        .format(instantTimestamp)

    override fun toString(): String {
        return buildString {
            append("Message(")
            append("Id=$id,")
            append("content=$content,")
            append("timestamp=$timestamp")
            append(")")
        }
    }
}

object Messages: Table() {
    val id = integer("id").autoIncrement()
    val content = varchar("content", 1024)
    val timestamp = timestamp("timestamp").default(Instant.now())

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}