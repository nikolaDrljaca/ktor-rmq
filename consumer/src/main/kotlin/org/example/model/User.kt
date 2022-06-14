package org.example.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users: IntIdTable() {
    val name = varchar("name", 56)
    val email = varchar("email", 128)
}

class User(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<User>(Users)
    var name by Users.name
    var email by Users.email

    //Now we have access to messages that belong to this user
    val messages by Message referrersOn Messages.userId
}