package ru.itmo.calls.controller.rest.user.models

data class ExtendedUserInfoDto(
    val id: Int,
    val name: String,
    val group: String,
    val photoUrl: String
)
