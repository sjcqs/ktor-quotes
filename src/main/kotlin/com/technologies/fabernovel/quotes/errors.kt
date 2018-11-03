package com.technologies.fabernovel.quotes

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

fun StatusPages.Configuration.exceptionFilters() {
    exception<MismatchedInputException> { cause ->
        val local = call.request.local
        val message = "Invalid request for ${local.method.value} ${local.uri}"
        call.respond(HttpStatusCode.BadRequest, errorResponse(InvalidRequestException(message)))
        throw cause
    }
    exception<InvalidCredentialsException> { exception ->
        call.respond(HttpStatusCode.Unauthorized, errorResponse(exception))
    }
    exception<InvalidRequestException> { exception ->
        call.respond(HttpStatusCode.BadRequest, errorResponse(exception))
    }
}

private fun errorResponse(exception: IllegalArgumentException) =
    mapOf("OK" to false, "error" to (exception.message ?: ""))

sealed class QuoteException(message: String) : IllegalArgumentException(message)

class InvalidRequestException(message: String) : QuoteException(message)

class InvalidCredentialsException(
    message: String = "Invalid credentials"
) : QuoteException(message)