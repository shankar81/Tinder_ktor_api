package database

import org.jetbrains.exposed.sql.Database

object DB {
    private val host = "localhost"
    private val port = 5555
    private val dbName = "tinder_db"
    private val dbUser = "tinder_user"
    private val dbPassword = "tinderpass123"

    init {
        val dbUrl = System.getenv("")
    }

    fun connect() = Database.connect(
        "jdbc:postgresql://$host:$port/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}