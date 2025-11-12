package ru.itmo.calls.controller.rest.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.calls.controller.rest.auth.mapping.toResponse
import ru.itmo.calls.controller.rest.auth.model.LoginRequest
import ru.itmo.calls.controller.rest.auth.model.LoginResponse
import ru.itmo.calls.usecase.LoginUseCase
import ru.itmo.calls.usecase.LogoutUseCase
import ru.itmo.calls.usecase.model.LoginCommand
import ru.itmo.calls.usecase.model.LogoutCommand

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase
) {
    companion object {
        private const val TOKEN_COOKIE_NAME = "AUTH_TOKEN"
        private const val MAX_AGE_SECONDS = 24 * 60 * 60
    }

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid request: LoginRequest,
        response: HttpServletResponse
    ): LoginResponse {
        val result = loginUseCase.login(
            LoginCommand(login = request.login, password = request.password),
        )

        // Сохраняем токен в cookie используя ResponseCookie для лучшей совместимости
        val cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, result.token)
            .path("/")
            .maxAge(MAX_AGE_SECONDS.toLong())
            .httpOnly(true)
            .secure(false) // Установите true, если используете HTTPS
            .sameSite("Lax") // Разрешаем отправку cookie при навигации
            .build()
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())

        return result.toResponse()
    }

    @PostMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        // Получаем токен из cookie или заголовка
        val token = extractToken(request)

        if (token != null) {
            logoutUseCase.logout(LogoutCommand(token))
        }

        // Удаляем cookie
        val cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, "")
            .path("/")
            .maxAge(0)
            .httpOnly(true)
            .secure(false)
            .sameSite("Lax")
            .build()
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
    }

    private fun extractToken(request: HttpServletRequest): String? {
        // Проверяем заголовок Authorization
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }

        // Проверяем cookie
        val cookies = request.cookies
        if (cookies != null) {
            cookies.firstOrNull { it.name == TOKEN_COOKIE_NAME }?.value?.let {
                return it
            }
        }

        return null
    }
}
