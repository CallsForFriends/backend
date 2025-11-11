package ru.itmo.calls.controller.rest.user.mapping

import ru.itmo.calls.controller.rest.user.models.UserInfoDto
import ru.itmo.calls.domain.user.UserInfo

fun UserInfo.toUserDto(): UserInfoDto {
    return UserInfoDto(
        userId = userId,
        fullName = fullName,
        photoUrl = photoUrl,
    )
}
