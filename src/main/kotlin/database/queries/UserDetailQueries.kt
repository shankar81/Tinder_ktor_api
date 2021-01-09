package database.queries

import asUserDetails
import database.tables.UserDetailsTable
import models.UserDetails
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface UserDetailService {
    fun create(details: UserDetails): UserDetails
    fun getDetail(userId: Int): UserDetails?
    fun getAllDetail(): List<UserDetails>
}

object UserDetailQueries : UserDetailService {
    override fun create(details: UserDetails): UserDetails {
        val detail = getDetail(details.userId)
        // If already exist send the existed one
        if (detail != null) {
            return detail
        }
        details.id = transaction {
            UserDetailsTable.insertAndGetId {
                it[userId] = details.userId
                it[token] = details.token
                it[userPhone] = details.phone
                it[userName] = details.name
                it[email] = details.email
                it[dob] = details.dob
                it[gender] = details.gender
                it[orientation] = details.orientation
                it[showMe] = details.showMe
                it[university] = details.university
                it[passions] = details.passions
            }
        }.value

        return details
    }

    override fun getDetail(userId: Int): UserDetails? {
        return transaction {
            val result = UserDetailsTable.select {
                UserDetailsTable.userId eq userId
            }

            result.firstOrNull()?.asUserDetails()
        }
    }

    override fun getAllDetail(): List<UserDetails> {
        return transaction {
            val result = UserDetailsTable.selectAll()

            result.map { it.asUserDetails() }
        }
    }
}