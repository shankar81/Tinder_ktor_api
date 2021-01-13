import database.tables.OTPTable
import database.tables.PassionTable
import database.tables.UserDetailsTable
import database.tables.UserTable
import io.ktor.application.*
import io.ktor.auth.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import models.OTP
import models.Passion
import models.User
import models.UserDetails
import org.jetbrains.exposed.sql.ResultRow
import java.io.InputStream
import java.io.OutputStream
import java.text.StringCharacterIterator

import java.text.CharacterIterator


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
    get(UserDetailsTable.showOrientations),
    get(UserDetailsTable.showGender),
    get(UserDetailsTable.showMe),
    get(UserDetailsTable.university),
    get(UserDetailsTable.passions),
)

// For file upload
suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}

fun Long.toMB(): String {
    var bytes = this
    if (-1000 < bytes && bytes < 1000) {
        return "$bytes B"
    }
    val ci: CharacterIterator = StringCharacterIterator("KMGTPE")
    while (bytes <= -999950 || bytes >= 999950) {
        bytes /= 1000
        ci.next()
    }
    return String.format("%.1f %cB", bytes / 1000.0, ci.current())
}