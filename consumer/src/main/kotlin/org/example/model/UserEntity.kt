package org.example.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable: IntIdTable() {
    val name = varchar("name", 56)
    val email = varchar("email", 128)
}

class UserEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<UserEntity>(UserTable)
    var name by UserTable.name
    var email by UserTable.email

    //Now we have access to messages that belong to this user
    val messages by MessageEntity referrersOn MessageTable.userId
}