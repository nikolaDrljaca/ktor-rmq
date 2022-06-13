package org.example.dao

import kotlinx.coroutines.Dispatchers
import org.example.model.Messages
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database by lazy {
            Database.connect(jdbcURL, driverClassName)
        }

        /*
        Create schemas for tables when the database is initialized.
        If the schemas are already present this code does nothing.
         */
        transaction(database) {
            SchemaUtils.create(Messages)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

}