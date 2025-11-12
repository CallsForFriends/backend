package ru.itmo.calls.usecase

import org.springframework.stereotype.Service
import ru.itmo.calls.port.UserInfoProvider
import ru.itmo.calls.usecase.model.FindUsersByFilterCommand
import ru.itmo.calls.usecase.model.FindUsersByFilterResult

@Service
class FindUsersByFilterUseCase(
    private val userInfoProvider: UserInfoProvider
) {
    fun find(command: FindUsersByFilterCommand): FindUsersByFilterResult {
        return FindUsersByFilterResult(
            users = userInfoProvider.findUserInfoByFilter(
                filter = command.filter,
                limit = command.limit,
                offset = command.offset,
            )
        )
    }
}
