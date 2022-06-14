package org.example.dao

import org.example.model.UserEntity

interface UserDao {

    suspend fun getAllUsers(): List<UserEntity>

    suspend fun insert(name: String, email: String): UserEntity

    suspend fun getUserById(id: Int): UserEntity?

    suspend fun updateUserInfo(id: Int, name: String, email: String): Boolean

    suspend fun deleteUser(id: Int): Boolean

}