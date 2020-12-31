package database.queries

import database.UserTable
import models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface UserService {
    suspend fun createUser(phone: String): Int
    suspend fun find(id: Int? = null, phone: String? = null): List<User>?
    suspend fun deleteUser(id: Int?): Int
    suspend fun updateUser(user: User): User
}

object UserQueries : UserService {
    override suspend fun createUser(phone: String): Int {
        return transaction {
            UserTable.insertAndGetId { user ->
                user[name] = "Unknown"
                user[UserTable.phone] = phone
            }
        }.value
    }

    override suspend fun find(id: Int?, phone: String?): List<User>? {
        return transaction {
            val result = when {
                id == null && phone == null -> UserTable.selectAll()
                id != null && phone != null -> {
                    UserTable.select {
                        UserTable.id eq id
                        UserTable.phone eq phone
                    }
                }
                id != null && phone == null -> UserTable.select { UserTable.id eq id }
                id == null && phone != null -> UserTable.select { UserTable.phone eq phone }
                else -> null
            }


            result?.map { row ->
                row.asUser()
            }
        }
    }

    override suspend fun deleteUser(id: Int?): Int {
        val users = find(id, null) ?: emptyList()

        return if (users.isEmpty()) {
            -1
        } else {
            transaction {
                UserTable.deleteWhere {
                    UserTable.id eq id
                }
            }
        }
    }

    override suspend fun updateUser(user: User): User {
        return transaction {
            UserTable.update({ UserTable.id eq user.id }) {
                it[name] = user.name
                it[phone] = user.phone
            }

            user
        }
    }
}


fun ResultRow.asUser() = User(get(UserTable.id).value, get(UserTable.name), get(UserTable.phone))