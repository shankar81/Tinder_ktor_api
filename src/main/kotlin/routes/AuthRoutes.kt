package routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.authRoutes() {
    // Send token after successful login with JWT
    post("/login") {
        call.respond("Auth login Route")
    }

    // Verify the if user is logged in
    get("/verify") {
        call.respond("Auth Verify Route")
    }
}