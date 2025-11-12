package ru.itmo.calls.usecase

import org.springframework.stereotype.Service
import ru.itmo.calls.port.AuthDataProvider
import ru.itmo.calls.usecase.model.FindCurrentUserResult

@Service
class FindCurrentUserUseCase(
    private val authDataProvider: AuthDataProvider
) {
    fun find(): FindCurrentUserResult {
        return FindCurrentUserResult(
            id = authDataProvider.getCurrentUserId()
        )
    }
}
