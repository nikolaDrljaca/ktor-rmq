package org.example.model

import org.jetbrains.exposed.sql.Table

data class Message(val id: Int, val content: String)

object Messages: Table() {
    val id = integer("id").autoIncrement()
    val content = varchar("content", 1024)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}