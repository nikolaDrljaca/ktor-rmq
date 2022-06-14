package org.example.dao

import org.example.model.MessageEntity

interface MessageDao {

    suspend fun getAllMessages(): List<MessageEntity>

    suspend fun insert(userId: Int, content: String): MessageEntity?

    suspend fun getMessageById(id: Int): MessageEntity?

    suspend fun updateMessage(id: Int, content: String): Boolean

    suspend fun deleteMessage(id: Int): Boolean

}