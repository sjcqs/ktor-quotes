package com.technologies.fabernovel.quotes.routes

import com.technologies.fabernovel.quotes.SuccessResponse
import io.ktor.application.ApplicationCall
import io.ktor.response.respond

suspend inline fun ApplicationCall.respondSuccess(isSuccessful: Boolean = true) = respond(SuccessResponse(isSuccessful))

