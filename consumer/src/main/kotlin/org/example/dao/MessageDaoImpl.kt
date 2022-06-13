package org.example.dao

import kotlinx.coroutines.runBlocking
import org.example.dao.DatabaseFactory.dbQuery
import org.example.model.Message
import org.example.model.Messages
import org.example.model.Messages.content
import org.example.model.Messages.id
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class MessageDaoImpl : MessageDao {
    private fun mapRowToMessage(row: ResultRow) = Message(
        id = row[Messages.id],
        content = row[Messages.content]
    )

    override suspend fun getAllMessages(): List<Message> {
        return dbQuery {
            Messages.selectAll().map { mapRowToMessage(it) }
        }
    }

    override suspend fun getMessageById(id: Int): Message? = dbQuery {
        Messages
            .select { Messages.id eq id } //eq less greater plus times inList notInList
            .map { mapRowToMessage(it) }
            .singleOrNull()
    }

    override suspend fun insert(content: String): Message? = dbQuery {
        val insertStatement = Messages.insert {
            it[Messages.content] = content
        }

        insertStatement.resultedValues?.singleOrNull()?.let { mapRowToMessage(it) }
    }


    override suspend fun updateMessage(id: Int, content: String): Boolean  = dbQuery {
        Messages.update(
            where = { Messages.id eq id },
            body = { it[Messages.content] = content }
        ) > 0
    }

    override suspend fun deleteMessage(id: Int): Boolean = dbQuery {
        Messages.deleteWhere { Messages.id eq id } > 0
    }
}

val messageDao: MessageDao = MessageDaoImpl().apply {
    runBlocking {
        if(getAllMessages().isEmpty())
            insert("Initial message, inserted at startup.")
    }
}
