package ru.itmo.calls.controller.rest.auth.mapping

import ru.itmo.calls.controller.rest.auth.model.LoginResponse
import ru.itmo.calls.port.model.LoginResult

fun LoginResult.toResponse(): LoginResponse {
    return LoginResponse(
        authToken = accessToken
    )
}