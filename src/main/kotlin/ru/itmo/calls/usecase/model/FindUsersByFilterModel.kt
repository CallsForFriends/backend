package ru.itmo.calls.usecase.model

import ru.itmo.calls.domain.user.UserInfo
import ru.itmo.calls.port.model.PagedCollection

data class FindUsersByFilterCommand(
    val filter: String,
    val limit: Int,
    val offset: Int
)

data class FindUsersByFilterResult(
    val users: PagedCollection<UserInfo>
)
