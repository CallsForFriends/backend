package ru.itmo.calls.usecase

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
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

    fun login(command: LoginCommand): LoginResult {
        val (login, password) = command

        val idToken = authProvider.getIdToken(login, password)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")

        val userInfo = jwtParsingService.parseItmoIdFromJwt(idToken)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials")

        // Проверяем, есть ли уже токен для этого пользователя
        val customToken = tokenService.findTokenByLogin(command.login)

        val token = if (customToken != null) {
            // Если токен уже существует, обновляем пароль и userId (на случай если они изменились)
            val tokenInfo = tokenService.getTokenInfo(customToken)
            if (tokenInfo != null) {
                // Обновляем пароль и userId в существующем токене
                tokenInfo.password = password
                tokenInfo.userId = userInfo.id
            }
            customToken
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
}
