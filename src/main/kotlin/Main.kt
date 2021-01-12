import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.SerializationFeature
import database.DB
import database.tables.OTPTable
import database.tables.PassionTable
import database.tables.UserDetailsTable
import database.tables.UserTable
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import models.Response
import models.Result
import models.User
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import routes.authRoutes
import routes.passionRoutes

private fun getVerifier() = JWT.require(Algorithm.HMAC256("MySecret")).build()

val PORT = System.getenv("PORT")?.toInt() ?: 8080

fun main() {
    val server = embeddedServer(Netty, PORT, module = Application::mainModule)

    server.start()
}

fun Application.mainModule() {
    DB.connect()

    transaction {
        SchemaUtils.create(UserTable)
        SchemaUtils.create(OTPTable)
        SchemaUtils.create(PassionTable)
        SchemaUtils.create(UserDetailsTable)
    }

    install(StatusPages) {
        status(HttpStatusCode.Unauthorized) {
            val response = Response(null, "Please enter valid credentials", Result.ERROR.ordinal)
            call.respond(HttpStatusCode.Unauthorized, response)
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Authentication) {
        jwt {
            realm = "Ktor JWT Authentication"
            verifier(getVerifier())
            validate {
                val id = it.payload.getClaim("id").asInt()
                val phone = it.payload.getClaim("phone").asString()
                if (phone != null) {
                    User(id, phone = phone)
                } else {
                    null
                }
            }
        }
    }

    routing {
        trace {
            application.log.info(it.buildText())
        }

        get("/") {
            call.respond("API is working fine!  !")
        }

        get("/dropTables") {
            transaction {
                SchemaUtils.drop(UserTable, OTPTable, UserDetailsTable)
            }
            call.respondText { "Tables Dropped successfully" }
        }
        passionRoutes()
        authRoutes()
    }
}
