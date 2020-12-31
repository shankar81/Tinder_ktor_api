package database

import org.jetbrains.exposed.dao.IntIdTable

object UserTable : IntIdTable() {
    val name = varchar("name", 50).default("Unknown")
    val phone = varchar("phone", 15).uniqueIndex()
}