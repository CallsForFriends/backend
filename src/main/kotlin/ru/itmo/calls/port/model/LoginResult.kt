package ru.itmo.calls.port.model

data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
)
