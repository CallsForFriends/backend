package ru.itmo.calls.controller.rest.auth.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)
