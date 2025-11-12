package ru.itmo.calls.usecase

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.itmo.calls.config.dto.ItmoIdDto
import ru.itmo.calls.port.AuthProvider
import ru.itmo.calls.service.JwtParsingService
import ru.itmo.calls.service.TokenService
import ru.itmo.calls.usecase.model.LoginCommand
import ru.itmo.calls.usecase.model.LoginResult

@Service
class LoginUseCase(
    private val authProvider: AuthProvider,
    private val tokenService: TokenService,
    private val jwtParsingService: JwtParsingService
) {
    private val log = KotlinLogging.logger { }

    fun login(command: LoginCommand): LoginResult {
        val (login, password) = command
        val authResult = authProvider.login(login, password)

        if (!authResult) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        // Получаем информацию о пользователе после успешной аутентификации
        val userInfo = getUserInfoAfterLogin(command.login, command.password)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get user info")

        // Проверяем, есть ли уже токен для этого пользователя
        val existingToken = tokenService.findTokenByLogin(command.login)

        val token = if (existingToken != null) {
            // Если токен уже существует, обновляем пароль и userId (на случай если они изменились)
            val tokenInfo = tokenService.getTokenInfo(existingToken)
            if (tokenInfo != null) {
                // Обновляем пароль и userId в существующем токене
                tokenInfo.password = password
                tokenInfo.userId = userInfo.id
            }
            existingToken
        } else {
            // Если токена нет, создаем новый
            tokenService.generateToken(
                user = userInfo,
                login = login,
                password = password
            )
        }

        return LoginResult(token)
    }

    private fun getUserInfoAfterLogin(username: String, password: String): ItmoIdDto? {
        return runCatching {
            val loggedMyItmo = authProvider.createMyItmo()
            loggedMyItmo.auth(username, password)
            return jwtParsingService.parseItmoIdFromJwt(loggedMyItmo.storage.idToken)
        }.onFailure {
            log.error(it) { "[LOGIN] Failed to get user info after login. Reason: ${it.message}" }
        }.getOrNull()
    }
}
