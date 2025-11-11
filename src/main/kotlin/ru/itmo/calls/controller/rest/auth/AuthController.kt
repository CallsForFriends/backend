package ru.itmo.calls.controller.rest.auth

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.calls.adapter.api.MyItmoAdapter
import ru.itmo.calls.controller.rest.auth.mapping.toResponse
import ru.itmo.calls.controller.rest.auth.model.LoginRequest
import ru.itmo.calls.controller.rest.auth.model.LoginResponse

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val itmoAdapter: MyItmoAdapter
) {
    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): LoginResponse {
        return itmoAdapter.login(request.login, request.password).toResponse()
    }
}
