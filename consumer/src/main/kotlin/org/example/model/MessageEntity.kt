package org.example.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

class MessageEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<MessageEntity>(MessageTable)
    var content by MessageTable.content
    var timestamp by MessageTable.timestamp
    var user by UserEntity referencedOn MessageTable.userId
}

/*
Object that describes the table entity for the database.
 */
object MessageTable: IntIdTable() {
    val content = varchar("content", 1024)
    val timestamp = timestamp("timestamp").default(Instant.now())

    //foreign key to the user. One user has many messages
    val userId = reference(
        name = "user_id",
        refColumn = UserTable.id,
        onDelete = ReferenceOption.CASCADE
    )
    // this is now an optional reference
    // user optionalReferencedOn on the other side
    //val userId = reference("user_id", Users.id).nullable()
}