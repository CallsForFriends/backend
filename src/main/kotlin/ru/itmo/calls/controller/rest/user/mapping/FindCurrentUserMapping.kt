package ru.itmo.calls.controller.rest.user.mapping

import ru.itmo.calls.controller.rest.user.models.ExtendedUserInfoDto
import ru.itmo.calls.controller.rest.user.models.FindCurrentUserResponse
import ru.itmo.calls.usecase.model.FindCurrentUserResult

fun FindCurrentUserResult.toResponse(): FindCurrentUserResponse {
    return FindCurrentUserResponse(
        user = ExtendedUserInfoDto(
            id = id,
        )
    )
}
