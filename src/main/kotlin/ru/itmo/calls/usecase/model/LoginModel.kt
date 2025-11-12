package ru.itmo.calls.usecase.model

data class LoginCommand(
    val login: String,
    val password: String
)

data class LoginResult(
    val token: String,
)
