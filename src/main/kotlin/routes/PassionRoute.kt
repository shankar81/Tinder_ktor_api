package routes

import database.queries.PassionQueries
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import models.Response
import models.Result

fun Route.passionRoutes() {
    get("/passions") {
        println("PASSIONS ROUTE")
        val response = Response(
            PassionQueries.getPassions(),
            "Passions List",
            Result.SUCCESS.ordinal
        )
        call.respond(response)
    }

}