package ru.itmo.calls.domain.user

data class UserInfo(
    val userId: Long,
    val fullName: String,
    val photoUrl: String,
)
