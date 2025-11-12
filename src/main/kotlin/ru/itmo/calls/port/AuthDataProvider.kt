package ru.itmo.calls.port

interface AuthDataProvider {
    fun getCurrentUserId(): Int
}
