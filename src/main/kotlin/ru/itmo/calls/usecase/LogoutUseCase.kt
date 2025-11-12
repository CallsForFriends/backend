package ru.itmo.calls.usecase

import org.springframework.stereotype.Service
import ru.itmo.calls.port.AuthProvider
import ru.itmo.calls.service.TokenService
import ru.itmo.calls.usecase.model.LogoutCommand

@Service
class LogoutUseCase(
    private val tokenService: TokenService,
    private val authProvider: AuthProvider
) {
    fun logout(command: LogoutCommand) {
        tokenService.removeToken(command.token)
        authProvider.removeMyItmo(command.token)
    }
}
