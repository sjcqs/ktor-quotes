package com.technologies.fabernovel.quotes

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm


object Auth {
    private const val SECRET_KEY = "secret"
    private val algorithm = Algorithm.HMAC256(SECRET_KEY)

    fun makeJwtVerifier(): JWTVerifier = JWT
        .require(algorithm)
        .build()

    fun sign(name: String): Map<String, String> {
        return mapOf("token" to JWT.create().withClaim("name", name).sign(algorithm))
    }

}


