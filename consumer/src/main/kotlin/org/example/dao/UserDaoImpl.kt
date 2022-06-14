package org.example.dao

import kotlinx.coroutines.runBlocking
import org.example.dao.DatabaseFactory.dbQuery
import org.example.model.User

class UserDaoImpl: UserDao {
    override suspend fun getAllUsers(): List<User> = dbQuery {
        User.all().toList()
    }

    override suspend fun insert(name: String, email: String): User  = dbQuery {
        User.new {
            this.name = name
            this.email = email
        }
    }

    override suspend fun getUserById(id: Int): User?  = dbQuery {
        User.findById(id)
    }

    override suspend fun updateUserInfo(id: Int, name: String, email: String): Boolean = dbQuery {
        User.findById(id)?.let {
            it.name = name
            it.email = email
            return@let true
        } ?: false
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        User.findById(id)?.let {
            it.delete()
            return@let true
        } ?: false
    }
}

val userDao: UserDao = UserDaoImpl().apply {
    runBlocking {
        if(getAllUsers().isEmpty())
            insert("Nikola", "nikola@aol.com")
    }
}