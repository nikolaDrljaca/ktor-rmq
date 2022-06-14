package org.example.dao

import org.example.model.Message

interface MessageDao {

    suspend fun getAllMessages(): List<Message>

    suspend fun insert(userId: Int, content: String): Message?

    suspend fun getMessageById(id: Int): Message?

    suspend fun updateMessage(id: Int, content: String): Boolean

    suspend fun deleteMessage(id: Int): Boolean

}