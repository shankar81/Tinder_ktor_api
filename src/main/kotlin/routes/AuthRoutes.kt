package routes

import database.queries.UserQueries
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.AuthResponse
import models.Response
import models.Result
import models.User
import org.jetbrains.exposed.sql.Database
import user

fun Route.authRoutes() {
    // Send token after successful login with JWT
    post("/login") {
        val user = call.receive<User>()
        val users = UserQueries.find(phone = user.phone)
        val response: Response<AuthResponse>

        /**
         * If user is not exist then only create user
         */
        if (users == null || users.isEmpty()) {
            val userId = UserQueries.createUser(user.phone)

            user.id = userId

            val token = user.generateToken()
            response = Response(AuthResponse(user, token), "Token is sent!", Result.SUCCESS.ordinal)
            call.respond(response)
        } else {
            response = Response(AuthResponse(user, null), "User Already exists", Result.ERROR.ordinal)
        }

        call.respond(response)
    }

    // Verify the if user is logged in
    authenticate {
        get("/verify") {
            val response = Response<AuthResponse>(
                AuthResponse(call.user, null),
                "Authenticated Successfully",
                Result.SUCCESS.ordinal
            )
            call.respond(HttpStatusCode.OK, response)
        }
    }

}