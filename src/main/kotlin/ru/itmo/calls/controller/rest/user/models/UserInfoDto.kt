package ru.itmo.calls.controller.rest.user.models

data class UserInfoDto(
    val userId: Long,
    val fullName: String,
    val photoUrl: String?,
)
