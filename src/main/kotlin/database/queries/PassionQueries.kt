package database.queries

import asPassion
import database.tables.PassionTable
import models.Passion
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

private interface PassionService {
    fun getPassions(): List<Passion>
    fun createPassion(passionName: String): Passion
}

object PassionQueries : PassionService {
    override fun getPassions(): List<Passion> {
        return transaction {
            val result = PassionTable.selectAll()

            result.map { it.asPassion() }
        }
    }

    override fun createPassion(passionName: String): Passion {
        val passion = getPassions().firstOrNull { it.name == passionName }
        if (passion != null) {
            return passion
        }
        val id = transaction {
            PassionTable.insertAndGetId {
                it[name] = passionName
            }
        }.value

        return Passion(id, passionName)
    }
}