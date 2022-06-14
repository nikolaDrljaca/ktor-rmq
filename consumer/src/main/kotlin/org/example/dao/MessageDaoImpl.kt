package org.example.dao

import kotlinx.coroutines.runBlocking
import org.example.dao.DatabaseFactory.dbQuery
import org.example.model.MessageEntity

class MessageDaoImpl(private val userDao: UserDao) : MessageDao {

    override suspend fun getAllMessages(): List<MessageEntity> {
        return dbQuery {
            MessageEntity.all().map { it }
        }
    }

    override suspend fun getMessageById(id: Int): MessageEntity? = dbQuery {
        MessageEntity.findById(id)
    }

    override suspend fun insert(userId: Int, content: String): MessageEntity? = dbQuery {
        val user = userDao.getUserById(userId)
        user?.let {
            MessageEntity.new {
                this.content = content
                this.user = it
            }
        }
    }


    override suspend fun updateMessage(id: Int, content: String): Boolean = dbQuery {
        MessageEntity.findById(id)?.let {
            it.content = content
            return@let true
        } ?: false
    }

    override suspend fun deleteMessage(id: Int): Boolean = dbQuery {
        MessageEntity.findById(id)?.let {
            it.delete()
            return@let true
        } ?: false
    }
}

val messageDao: MessageDao = MessageDaoImpl(userDao).apply {
    runBlocking {
        if(getAllMessages().isEmpty())
            insert(1, "Initial message, inserted at startup.")
    }
}
