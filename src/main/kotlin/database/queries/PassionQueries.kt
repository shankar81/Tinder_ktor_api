package database.queries

import asPassion
import database.PassionTable
import models.Passion
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

private interface PassionService {
    fun getPassions(): List<Passion>
    fun createPassion(passionName: String): Int
}

object PassionQueries : PassionService {
    override fun getPassions(): List<Passion> {
        return transaction {
            PassionTable.selectAll()
        }.map { it.asPassion() }
    }

    override fun createPassion(passionName: String): Int {
        return transaction {
            PassionTable.insertAndGetId {
                it[name] = passionName
            }
        }.value
    }
}