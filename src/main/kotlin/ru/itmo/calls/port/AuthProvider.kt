package ru.itmo.calls.port

interface AuthProvider {
    fun getCurrentUserId(): Int
}