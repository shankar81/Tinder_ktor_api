package routes

import database.queries.PassionQueries
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import models.Passion
import models.Response
import models.Result

fun Route.passionRoutes() {
    route("/passions") {

        get("") {
            println("PASSIONS ROUTE")
            val response = Response(
                PassionQueries.getPassions(),
                "Passions List",
                Result.SUCCESS.ordinal
            )
            call.respond(response)
        }

        post("") {
            val passion = call.receive<Passion>()
            val newPassion = PassionQueries.createPassion(passion.name)
            val response = Response(
                newPassion,
                "Passion Added Successfully",
                Result.SUCCESS.ordinal
            )
            call.respond(response)
        }
    }

}