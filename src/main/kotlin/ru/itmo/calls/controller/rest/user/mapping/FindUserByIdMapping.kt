package ru.itmo.calls.controller.rest.user.mapping

import ru.itmo.calls.controller.rest.user.models.FindUserByIdResponse
import ru.itmo.calls.usecase.model.FindUserByIdResult

fun FindUserByIdResult.toResponse(): FindUserByIdResponse {
    return FindUserByIdResponse(
        user = user.toUserDto()
    )
}
