package ru.itmo.calls.controller.rest.user.mapping

import ru.itmo.calls.controller.rest.user.models.FindUsersByFilterResponse
import ru.itmo.calls.usecase.model.FindUsersByFilterResult

fun FindUsersByFilterResult.toResponse(): FindUsersByFilterResponse {
    return FindUsersByFilterResponse(
        limit = users.limit,
        offset = users.offset,
        content = users.collection.map { it.toUserDto() },
        total = users.total
    )
}
