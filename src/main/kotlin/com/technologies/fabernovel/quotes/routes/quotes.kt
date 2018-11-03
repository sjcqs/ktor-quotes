package com.technologies.fabernovel.quotes.routes

import com.technologies.fabernovel.quotes.InvalidRequestException
import com.technologies.fabernovel.quotes.repo.quote.Quote
import com.technologies.fabernovel.quotes.repo.quote.QuoteRepository
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext

private const val QUOTE_ENDPOINT = "/quotes"
private const val PARAM_ID = "id"

fun Routing.quotes() {
    get(QUOTE_ENDPOINT) {
        call.respond(mapOf("quotes" to QuoteRepository.getAll()))
    }

    get("$QUOTE_ENDPOINT/{$PARAM_ID}") {
        val id = getIdParam()
        call.respond(mapOf("quote" to QuoteRepository.get(id)))
    }

    authenticate {
        post(QUOTE_ENDPOINT) {
            val quote = Quote(call.receive())
            val addedQuote = QuoteRepository.add(quote)
            call.respond(mapOf("quote" to addedQuote))
        }
    }

    authenticate {
        delete(QUOTE_ENDPOINT) {
            QuoteRepository.removeAll()
            call.respondSuccess()
        }
    }

    authenticate {
        delete("$QUOTE_ENDPOINT/{$PARAM_ID}") {
            val id = getIdParam()
            call.respondSuccess(QuoteRepository.remove(id))
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
private inline fun PipelineContext<Unit, ApplicationCall>.getIdParam() =
    call.parameters[PARAM_ID] ?: throw InvalidRequestException("No quote id")