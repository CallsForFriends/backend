package ru.itmo.calls.usecase.model

import ru.itmo.calls.domain.user.UserInfo

data class GetFavouriteUsersResult(
    val users: List<UserInfo>,
)
