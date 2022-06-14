package org.example.dao

import kotlinx.coroutines.runBlocking
import org.example.dao.DatabaseFactory.dbQuery
import org.example.model.UserEntity

class UserDaoImpl: UserDao {
    override suspend fun getAllUsers(): List<UserEntity> = dbQuery {
        UserEntity.all().toList()
    }

    override suspend fun insert(name: String, email: String): UserEntity  = dbQuery {
        UserEntity.new {
            this.name = name
            this.email = email
        }
    }

    override suspend fun getUserById(id: Int): UserEntity?  = dbQuery {
        UserEntity.findById(id)
    }

    override suspend fun updateUserInfo(id: Int, name: String, email: String): Boolean = dbQuery {
        UserEntity.findById(id)?.let {
            it.name = name
            it.email = email
            return@let true
        } ?: false
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        UserEntity.findById(id)?.let {
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