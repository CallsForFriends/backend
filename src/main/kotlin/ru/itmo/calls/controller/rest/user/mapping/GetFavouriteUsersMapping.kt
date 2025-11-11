package ru.itmo.calls.controller.rest.user.mapping

import ru.itmo.calls.controller.rest.user.models.GetFavouriteUsersResponse
import ru.itmo.calls.usecase.model.GetFavouriteUsersResult

fun GetFavouriteUsersResult.toResponse(): GetFavouriteUsersResponse {
    return GetFavouriteUsersResponse(
        users = users.map { it.toUserDto() }
    )
}
