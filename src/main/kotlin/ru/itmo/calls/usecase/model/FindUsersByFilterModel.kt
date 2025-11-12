package ru.itmo.calls.usecase.model

import ru.itmo.calls.domain.user.UserInfo

data class FindUsersByFilterCommand(
    val filter: String,
    val limit: Int,
    val offset: Int
)

data class FindUsersByFilterResult(
    val users: List<UserInfo>
)
