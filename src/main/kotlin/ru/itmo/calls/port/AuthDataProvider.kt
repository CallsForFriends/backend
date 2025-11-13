package ru.itmo.calls.port

import ru.itmo.calls.port.model.CurrentUserInfo

interface AuthDataProvider {
    fun getCurrentUserInfo(): CurrentUserInfo
    fun getCurrentUserId(): Int
}
