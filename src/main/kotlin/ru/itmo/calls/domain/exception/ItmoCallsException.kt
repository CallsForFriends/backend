package ru.itmo.calls.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.server.ResponseStatusException

open class ItmoCallsException(
    reason: String,
    statusCode: HttpStatusCode = HttpStatus.UNPROCESSABLE_ENTITY
): ResponseStatusException(statusCode, reason)
