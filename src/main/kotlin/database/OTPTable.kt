package database

import org.jetbrains.exposed.dao.IntIdTable

object OTPTable : IntIdTable() {
    val otp = varchar("otp", 6)
    val phone = varchar("phone", 15)
}