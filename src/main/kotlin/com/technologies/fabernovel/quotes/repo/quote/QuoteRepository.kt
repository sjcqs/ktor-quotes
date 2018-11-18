package com.technologies.fabernovel.quotes.repo.quote

import com.technologies.fabernovel.quotes.QuoteNotFoundException
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger


object QuoteRepository {
    private val idCounter = AtomicInteger()
    private val quotes = CopyOnWriteArraySet<Quote>()

    fun add(quote: Quote): Quote {
        if (quotes.contains(quote)) {
            return quote
        }
        val id = idCounter.incrementAndGet()
        val addedQuote = quote.copy(id = id)
        quotes.add(addedQuote)
        return addedQuote
    }

    fun get(id: String): Quote = quotes.find {
        it.id.toString() == id
    } ?: throw QuoteNotFoundException("No quote with id: $id")

    fun get(id: Int): Quote = get(id.toString())

    fun getAll(): List<Quote> = quotes.toList()

    fun remove(quote: Quote): Boolean {
        if (!quotes.remove(quote)) {
            throw QuoteNotFoundException("Quote is not in the repoo")
        }
        return true
    }

    fun remove(id: String): Boolean = remove(get(id))

    fun remove(id: Int): Boolean = remove(get(id))

    fun removeAll() {
        quotes.clear()
    }

}