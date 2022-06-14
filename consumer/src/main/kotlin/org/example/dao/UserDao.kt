package org.example.dao

import org.example.model.User

interface UserDao {

    suspend fun getAllUsers(): List<User>

    suspend fun insert(name: String, email: String): User

    suspend fun getUserById(id: Int): User?

    suspend fun updateUserInfo(id: Int, name: String, email: String): Boolean

    suspend fun deleteUser(id: Int): Boolean

}