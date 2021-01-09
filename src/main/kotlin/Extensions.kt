import database.tables.OTPTable
import database.tables.PassionTable
import database.tables.UserDetailsTable
import database.tables.UserTable
import io.ktor.application.*
import io.ktor.auth.*
import models.OTP
import models.Passion
import models.User
import models.UserDetails
import org.jetbrains.exposed.sql.ResultRow

val ApplicationCall.user get() = authentication.principal<User>()!!

fun ResultRow.asOTP() = OTP(get(OTPTable.id).value, get(OTPTable.otp), get(OTPTable.phone))

fun ResultRow.asUser() = User(get(UserTable.id).value, get(UserTable.name), get(UserTable.phone))

fun ResultRow.asPassion() = Passion(get(PassionTable.id).value, get(PassionTable.name))

fun ResultRow.asUserDetails() = UserDetails(
    get(UserDetailsTable.id).value,
    get(UserDetailsTable.userId),
    get(UserDetailsTable.token),
    get(UserDetailsTable.userPhone),
    get(UserDetailsTable.userName),
    get(UserDetailsTable.email),
    get(UserDetailsTable.dob),
    get(UserDetailsTable.gender),
    get(UserDetailsTable.orientation),
    get(UserDetailsTable.showMe),
    get(UserDetailsTable.university),
    get(UserDetailsTable.passions),
)