package ru.itmo.calls.usecase

import org.springframework.stereotype.Service
import ru.itmo.calls.port.UserInfoProvider
import ru.itmo.calls.usecase.model.FindUserByIdCommand
import ru.itmo.calls.usecase.model.FindUserByIdResult

@Service
class FindUserByIdUseCase(
    private val userInfoProvider: UserInfoProvider
) {
    fun find(command: FindUserByIdCommand): FindUserByIdResult {
        return FindUserByIdResult(
            user = userInfoProvider.getUserInfo(command.id)
        )
    }
}
