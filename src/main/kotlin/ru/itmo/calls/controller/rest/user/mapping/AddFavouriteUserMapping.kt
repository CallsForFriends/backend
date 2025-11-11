package ru.itmo.calls.controller.rest.user.mapping

import ru.itmo.calls.controller.rest.user.models.AddFavouriteUserRequest
import ru.itmo.calls.usecase.model.AddFavouriteUserCommand

fun AddFavouriteUserRequest.toCommand(): AddFavouriteUserCommand {
    return AddFavouriteUserCommand(
        favouriteUserId = userId
    )
}