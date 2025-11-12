package ru.itmo.calls.controller.rest.error

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import ru.itmo.calls.controller.rest.error.model.ErrorResponse

@RestControllerAdvice
class RestErrorHandler {
    private val log = KotlinLogging.logger { }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleError(exception: ResponseStatusException): ResponseEntity<ErrorResponse> {
        log.error(exception) { exception.message }

        return ResponseEntity.status(exception.statusCode)
            .body(ErrorResponse(exception.message))
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleError(exception: Exception): ErrorResponse {
        log.error(exception) { exception.message }

        return ErrorResponse(exception.message ?: "Unknown error")
    }
}
