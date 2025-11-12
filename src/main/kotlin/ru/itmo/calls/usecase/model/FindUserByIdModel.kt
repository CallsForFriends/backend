package ru.itmo.calls.usecase.model

import ru.itmo.calls.domain.user.UserInfo

data class FindUserByIdCommand(
    val id: Int
)

data class FindUserByIdResult(
    val user: UserInfo
)
