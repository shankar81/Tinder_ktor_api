package routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.Response
import models.Result
import models.User
import user

fun Route.authRoutes() {
    // Send token after successful login with JWT
    post("/login") {
        val user = call.receive<User>()
        val token = user.generateToken()
        val response = Response(token, "Token is send!", Result.SUCCESS.ordinal)
        call.respond(response)
    }

    // Verify the if user is logged in
    authenticate {
        get("/verify") {
            val response = Response(call.user, "Authenticated Successfully", Result.SUCCESS.ordinal)
            call.respond(HttpStatusCode.OK, response)
        }
    }

}