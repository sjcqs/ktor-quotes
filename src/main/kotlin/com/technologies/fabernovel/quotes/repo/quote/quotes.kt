package com.technologies.fabernovel.quotes.repo.quote

data class Quote(
    val author: String,
    val text: String,
    val id: Int = -1
) {
    constructor(quote: QuoteCreate) : this(quote.author, quote.text)
}

data class QuoteCreate(
    val author: String,
    val text: String
)
