package com.technologies.fabernovel.quotes.repo.user

import com.technologies.fabernovel.quotes.InvalidCredentialsException
import com.technologies.fabernovel.quotes.LOG

object UserRepository {
    private val users = listOf(User("admin", "passw0rd"))
        .associateBy { it.name }
        .toMutableMap()

    @Throws(InvalidCredentialsException::class)
    fun validate(name: String, password: String) {
        val user = users[name]
        LOG.debug("$user $name $password")
        if (user == null || user.password != password) {
            throw InvalidCredentialsException()
        }
    }

    fun create(name: String, password: String) {
        if (users.containsKey(name)) {
            throw InvalidCredentialsException("User already exists")
        }
        users[name] = User(name, password)
    }
}