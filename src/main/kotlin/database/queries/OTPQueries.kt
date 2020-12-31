package database.queries

import database.OTPTable
import models.OTP
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

interface OTPService {
    fun create(phone: String): OTP
    fun update(otp: OTP): OTP
    fun find(phone: String): OTP?
}

private fun generateOTP(): String {
    val number = StringBuilder()
    for (i in 0..5) {
        number.append((0 until 10).random())
    }
    return number.toString()
}

object OTPQueries : OTPService {
    override fun create(phone: String): OTP {
        val number = generateOTP()
        val id = transaction {
            OTPTable.insertAndGetId {
                it[OTPTable.phone] = phone
                it[otp] = number
            }
        }

        return OTP(id.value, number, phone)
    }

    override fun update(otp: OTP): OTP {
        val number = generateOTP()
        val id = transaction {
            OTPTable.update({ OTPTable.id eq otp.id }) {
                it[OTPTable.otp] = number
            }
        }

        otp.id = id
        otp.otp = number

        return otp
    }

    override fun find(phone: String): OTP? {
        return transaction {
            val row = OTPTable.select {
                OTPTable.phone eq phone
            }

            if (row.empty()) {
                null
            } else {
                row.map { it.asOTP() }[0]
            }
        }
    }
}

fun ResultRow.asOTP() = OTP(get(OTPTable.id).value, get(OTPTable.otp), get(OTPTable.phone))