package ru.itmo.calls.controller.rest.user.mapping

import ru.itmo.calls.controller.rest.user.models.FindUsersByFilterResponse
import ru.itmo.calls.usecase.model.FindUsersByFilterResult

fun FindUsersByFilterResult.toResponse(limit: Int, offset: Int): FindUsersByFilterResponse {
    return FindUsersByFilterResponse(
        limit = limit,
        offset = offset,
        content = users.map { it.toUserDto() },
        total = users.size
    )
}
