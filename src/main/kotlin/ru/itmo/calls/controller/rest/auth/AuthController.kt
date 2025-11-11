package ru.itmo.calls.controller.rest.auth

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import ru.itmo.calls.adapter.ItmoAdapter
import ru.itmo.calls.controller.rest.auth.mapping.toResponse
import ru.itmo.calls.controller.rest.auth.model.LoginRequest
import ru.itmo.calls.controller.rest.auth.model.LoginResponse

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val itmoAdapter: ItmoAdapter
) {
    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): LoginResponse {
        return itmoAdapter.login(request.login, request.password).toResponse()
    }
}
