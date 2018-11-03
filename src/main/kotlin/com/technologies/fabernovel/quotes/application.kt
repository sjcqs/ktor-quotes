package com.technologies.fabernovel.quotes

import com.fasterxml.jackson.databind.SerializationFeature
import com.technologies.fabernovel.quotes.repo.user.User
import com.technologies.fabernovel.quotes.repo.user.UserRegister
import com.technologies.fabernovel.quotes.repo.user.UserRepository
import com.technologies.fabernovel.quotes.routes.quotes
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import org.slf4j.event.Level
import java.text.DateFormat
import java.time.Duration

private val verifier = Auth.makeJwtVerifier()
fun Application.main() {
    setup()
    routes()
}

private fun Application.setup() {
    install(DefaultHeaders)
    install(CallLogging) {
        level = Level.INFO
    }
    install(CORS) {
        maxAge = Duration.ofDays(1)
    }
    install(ContentNegotiation) {
        jackson {
            dateFormat = DateFormat.getDateInstance(DateFormat.LONG)
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(StatusPages) {
        exceptionFilters()
    }
    install(Authentication) {
        jwt {
            verifier(verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim("name").asString())
            }
        }
    }
    install(Routing) {
        authenticate {
            route("/who") {
                handle {
                    val principal = call.authentication.principal<JWTPrincipal>()
                    val subjectString = principal!!.payload.subject.removePrefix("auth0|")
                    call.respondText("Success, $subjectString")
                }
            }
        }
    }
}

private fun Application.routes() {
    routing {
        post("/user/login") {
            val user = call.receive<User>()
            UserRepository.validate(user.name, user.password)
            call.respond(Auth.sign(user.name))
        }
        post("/user/register") {
            val user = call.receive<UserRegister>()
            UserRepository.create(user.name, user.password)
            call.respond(Auth.sign(user.name))
        }
        quotes()
        get("/") {
            throw IllegalArgumentException()
        }
    }
}

data class SuccessResponse(
    val success: Boolean
)
