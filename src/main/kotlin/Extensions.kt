import database.OTPTable
import database.UserTable
import io.ktor.application.*
import io.ktor.auth.*
import models.OTP
import models.Passion
import models.User
import org.jetbrains.exposed.sql.ResultRow

val ApplicationCall.user get() = authentication.principal<User>()!!

fun ResultRow.asOTP() = OTP(get(OTPTable.id).value, get(OTPTable.otp), get(OTPTable.phone))

fun ResultRow.asUser() = User(get(UserTable.id).value, get(UserTable.name), get(UserTable.phone))

fun ResultRow.asPassion() = Passion(get(UserTable.id).value, get(UserTable.name))