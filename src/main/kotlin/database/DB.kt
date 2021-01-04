package database

import org.jetbrains.exposed.sql.Database
import java.net.URI

object DB {
    private val host: String
    private val port: Int
    private val dbName: String
    private val dbUser: String
    private val dbPassword: String

    init {
        val dbUrl = System.getenv("DATABASE_URL")

        if (dbUrl != null) {
            val dbUri = URI(dbUrl)
            host = dbUri.host
            port = dbUri.port
            dbName = dbUri.path.substring(1)
            val userInfo = dbUri.userInfo.split(":")
            dbUser = userInfo[0]
            dbPassword = userInfo[1]
        } else {
            host = System.getenv("DB_HOST") ?: "localhost"
            port = System.getenv("DB_PORT")?.toInt() ?: 8080
            dbName = System.getenv("DB_NAME") ?: "tinderr_db"
            dbUser = System.getenv("DB_USER") ?: "tinderr_user"
            dbPassword = System.getenv("DB_PASSWORD") ?: "tinderrpass123"
        }
    }

    fun connect() = Database.connect(
        "jdbc:postgresql://$host:$port/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}