package routes

import database.queries.OTPQueries
import database.queries.UserDetailQueries
import database.queries.UserQueries
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.*
import user

fun Route.authRoutes() {
    // Send token after successful login with JWT
    post("/login") {
        val user = call.receive<User>()
        val users = UserQueries.find(phone = user.phone)
        val userId: Int
        /**
         * If user is not exist then only create user
         */
        if (users == null || users.isEmpty()) {
            userId = UserQueries.createUser(user.phone)
            user.id = userId
        } else {
            user.id = users[0].id
        }

        // Generate OTP
        var otp = OTPQueries.find(user.phone)
        otp = if (otp != null) {
            OTPQueries.update(otp)
        } else {
            OTPQueries.create(user.phone)
        }

        // @TODO Sent OTP SMS
        // ...
        val response = Response(OTPResponse(user, otp.otp), "OTP has been sent!", Result.SUCCESS.ordinal)
        call.respond(response)
    }

    post("/verifyOtp") {
        val requestUser = call.receive<AuthRequest>()
        val users = UserQueries.find(phone = requestUser.phone)

        val response: Response<AuthResponse?> = if (users == null || users.isEmpty()) {
            Response(null, "OTP Verification failed", Result.ERROR.ordinal)
        } else {
            val user = users[0]
            val otp = OTPQueries.find(user.phone)

            if (otp != null && otp.otp == requestUser.otp && otp.phone == requestUser.phone) {
                val token = user.generateToken()
                Response(AuthResponse(user, token), "Token is sent!", Result.SUCCESS.ordinal)
            } else {
                Response(null, "OTP Verification failed", Result.ERROR.ordinal)
            }
        }
        call.respond(response)
    }

    // Verify the if user is logged in
    authenticate {
        get("/verify") {
            val response = Response(
                AuthResponse(call.user, null),
                "Authenticated Successfully",
                Result.SUCCESS.ordinal
            )
            call.respond(HttpStatusCode.OK, response)
        }
    }

    route("/userDetails") {
        // Save user data
        authenticate {
            post("") {
                val userDetail = call.receive<UserDetails>()

                val newDetail = UserDetailQueries.create(userDetail)

                call.respond(Response(newDetail, "Created user detail", Result.SUCCESS.ordinal))
            }
        }

        // Send logged in user data
        authenticate {
            get("") {
                val detail = UserDetailQueries.getDetail(call.user.id)

                call.respond(Response(detail, "Detail of ${call.user.id}", Result.SUCCESS.ordinal))
            }
        }

        // Send all users data
        authenticate {
            get("/all") {
                val details = UserDetailQueries.getAllDetail()

                call.respond(Response(details, "All user details", Result.SUCCESS.ordinal))
            }
        }
    }
}