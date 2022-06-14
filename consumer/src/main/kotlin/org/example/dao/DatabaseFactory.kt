package org.example.dao

import kotlinx.coroutines.Dispatchers
import org.example.model.MessageTable
import org.example.model.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
/*
For connections to PostgreSQL, we can use environment variables passed when starting the
docker container to give url(host will be the docker network, hostname) username and password.

 */
object DatabaseFactory {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://localhost:5432/rmq"
        val username = "postgres"
        val password = "nikola123"
        val database by lazy {
            Database.connect(
                url = jdbcURL,
                driver = driverClassName,
                user = username,
                password = password
            )
        }

        /*
        Create schemas for tables when the database is initialized.
        If the schemas are already present this code does nothing.
         */
        transaction(database) {
            SchemaUtils.create(UserTable, MessageTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

}