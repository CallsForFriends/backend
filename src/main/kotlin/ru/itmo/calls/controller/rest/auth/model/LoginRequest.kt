package ru.itmo.calls.controller.rest.auth.model

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank
    val login: String,
    @field:NotBlank
    val password: String
)
