package ru.itmo.calls.domain.exception

import org.springframework.http.HttpStatus

class UserNotFoundException(
    reason: String
): ItmoCallsException(reason, HttpStatus.NOT_FOUND) {
    companion object {
        fun forUserId(userId: Int): UserNotFoundException {
            return UserNotFoundException("User $userId not found")
        }
    }
}
