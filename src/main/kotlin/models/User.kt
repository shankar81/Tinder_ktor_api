package models

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.*
import java.util.*

data class User(val phone: String) : Principal {
    private val algorithm = Algorithm.HMAC256("MySecret")

    fun generateToken(): String = JWT.create()
        .withSubject("AuthenticateWithPhone")
        .withIssuer("ShankarTinderIssuer")
        .withClaim("phone", phone)
//        .withExpiresAt(getDuration()) // By default 24 HR
        .sign(algorithm)

//    private fun getDuration() = Date(System.currentTimeMillis() + 36000000) // 10 hours

}