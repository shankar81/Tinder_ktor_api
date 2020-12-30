import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

const val PORT = 8080
fun main() {
    val server = embeddedServer(Netty, PORT, module = Application::mainModule)

    server.start()
}

fun Application.mainModule() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

}