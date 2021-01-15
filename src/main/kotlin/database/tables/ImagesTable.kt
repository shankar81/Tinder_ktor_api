package database.tables

import org.jetbrains.exposed.dao.IntIdTable

object ImagesTable : IntIdTable() {
    val userId = integer("userId").uniqueIndex()
    val images = varchar("images", 200)
}