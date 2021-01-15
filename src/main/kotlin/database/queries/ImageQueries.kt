package database.queries

import database.tables.ImagesTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

interface ImageService {
    fun addImages(id: Int, imageList: ArrayList<String>)
}

object ImageQueries : ImageService {
    override fun addImages(id: Int, imageList: ArrayList<String>) {
        transaction {
            ImagesTable.insertAndGetId {
                it[userId] = id
                it[images] = imageList.joinToString(",")
            }
        }
    }
}