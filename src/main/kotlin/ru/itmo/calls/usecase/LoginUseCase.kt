package ru.itmo.calls.usecase

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.itmo.calls.port.AuthProvider
import ru.itmo.calls.port.UserInfoProvider
import ru.itmo.calls.service.TokenService
import ru.itmo.calls.usecase.model.LoginCommand
import ru.itmo.calls.usecase.model.LoginResult

@Service
class LoginUseCase(
    private val authProvider: AuthProvider,
    private val userInfoProvider: UserInfoProvider,
    private val tokenService: TokenService
) {
    private val log = KotlinLogging.logger { }

    fun login(command: LoginCommand): LoginResult {
        val (login, password) = command
        val authResult = authProvider.login(login, password)

        if (!authResult) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        // Получаем userId после успешной аутентификации
        val userId = getUserIdAfterLogin(command.login, command.password)
            ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get user ID")

        // Проверяем, есть ли уже токен для этого пользователя
        val existingToken = tokenService.findTokenByLogin(command.login)

        val token = if (existingToken != null) {
            // Если токен уже существует, обновляем пароль и userId (на случай если они изменились)
            val tokenInfo = tokenService.getTokenInfo(existingToken)
            if (tokenInfo != null) {
                // Обновляем пароль и userId в существующем токене
                tokenInfo.password = password
                tokenInfo.userId = userId
            }
            existingToken
        } else {
            // Если токена нет, создаем новый
            tokenService.generateToken(
                userId = userId,
                login = login,
                password = password
            )
        }

        return LoginResult(token)
    }


    fun getUserIdAfterLogin(username: String, password: String): Int? {
        return runCatching {
            val loggedMyItmo = authProvider.createMyItmo()
            loggedMyItmo.auth(username, password)

            return userInfoProvider.getUserInfo(username.toInt(), loggedMyItmo).userId.toInt()
        }.onFailure {
            log.error(it) { "[LOGIN] Failed to get user ID after login. Reason: ${it.message}" }
        }.getOrNull()
    }
}
