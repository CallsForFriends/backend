package ru.itmo.calls.controller.rest.auth.mapping

import ru.itmo.calls.controller.rest.auth.model.LoginResponse
import ru.itmo.calls.usecase.model.LoginResult

fun LoginResult.toResponse(): LoginResponse {
    return LoginResponse(
        authToken = token
    )
}
