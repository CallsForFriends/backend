package ru.itmo.calls.usecase.model

data class FindCurrentUserResult(
    val id: Int,
    val name: String,
    val group: String,
    val photoUrl: String
)
