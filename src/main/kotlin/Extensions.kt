import io.ktor.application.*
import io.ktor.auth.*
import models.User

val ApplicationCall.user get() = authentication.principal<User>()!!