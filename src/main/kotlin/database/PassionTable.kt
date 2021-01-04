package database

import org.jetbrains.exposed.dao.IntIdTable

object PassionTable : IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
}