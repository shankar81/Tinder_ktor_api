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
        host ="localhost"
        port = 5555
        dbName = "tinderr_db"
        dbUser = "tinderr_user"
        dbPassword = "tinderrpass123"

//        val dbUrl = System.getenv("DATABASE_URL")
//
//        if (dbUrl != null) {
//            val dbUri = URI(dbUrl)
//            host = dbUri.host
//            port = dbUri.port
//            dbName = dbUri.path.substring(1)
//            val userInfo = dbUri.userInfo.split(":")
//            dbUser = userInfo[0]
//            dbPassword = userInfo[1]
//        } else {
//            host = System.getenv("DB_HOST")
//            port = System.getenv("DB_PORT").toInt()
//            dbName = System.getenv("DB_NAME")
//            dbUser = System.getenv("DB_USER")
//            dbPassword = System.getenv("DB_PASSWORD")
//        }
    }

    fun connect() = Database.connect(
        "jdbc:postgresql://$host:$port/$dbName",
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
}