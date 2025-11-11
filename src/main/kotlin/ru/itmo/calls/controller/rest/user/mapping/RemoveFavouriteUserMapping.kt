package ru.itmo.calls.controller.rest.user.mapping

import ru.itmo.calls.controller.rest.user.models.RemoveFavouriteUserRequest
import ru.itmo.calls.usecase.model.RemoveFavouriteUserCommand

fun RemoveFavouriteUserRequest.toCommand(): RemoveFavouriteUserCommand {
    return RemoveFavouriteUserCommand(
        favouriteUserId = userId,
    )
}
