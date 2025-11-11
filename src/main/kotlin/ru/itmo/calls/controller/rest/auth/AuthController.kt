package ru.itmo.calls.controller.rest.auth

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import ru.itmo.calls.adapter.api.MyItmoAdapter
import ru.itmo.calls.controller.rest.auth.model.LoginRequest
import ru.itmo.calls.controller.rest.auth.model.LoginResponse
import ru.itmo.calls.service.TokenService

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val itmoAdapter: MyItmoAdapter,
    private val tokenService: TokenService
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
        // Проверяем через myItmo.auth
        val authResult = itmoAdapter.login(request.login, request.password)

        if (!authResult) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        // Проверяем, есть ли уже токен для этого пользователя
        val existingToken = tokenService.findTokenByLogin(request.login)

        val token = if (existingToken != null) {
            // Если токен уже существует, обновляем пароль (на случай если он изменился)
            val tokenInfo = tokenService.getTokenInfo(existingToken)
            if (tokenInfo != null) {
                // Обновляем пароль в существующем токене
                tokenInfo.password = request.password
            }
            existingToken
        } else {
            // Если токена нет, создаем новый
            tokenService.generateToken(
                userId = request.login, // Используем login как userId
                login = request.login,
                password = request.password
            )
        }

        // Сохраняем токен в cookie используя ResponseCookie для лучшей совместимости
        val cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, token)
            .path("/")
            .maxAge(MAX_AGE_SECONDS.toLong())
            .httpOnly(true)
            .secure(false) // Установите true, если используете HTTPS
            .sameSite("Lax") // Разрешаем отправку cookie при навигации
            .build()
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())

        return LoginResponse(
            authToken = token
        )
    }

    @PostMapping("/logout")
    fun logout(
        request: jakarta.servlet.http.HttpServletRequest,
        response: HttpServletResponse
    ) {
        // Получаем токен из cookie или заголовка
        val token = extractToken(request)

        if (token != null) {
            // Удаляем токен из системы
            tokenService.removeToken(token)
            // Удаляем кэшированный MyItmo экземпляр
            itmoAdapter.removeMyItmo(token)
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

    private fun extractToken(request: jakarta.servlet.http.HttpServletRequest): String? {
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
