package ru.itmo.calls.controller.rest.user.models

data class FindUsersByFilterResponse(
    val limit: Int,
    val offset: Int,
    val content: List<UserInfoDto>,
    val total: Int,
)
