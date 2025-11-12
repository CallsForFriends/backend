package ru.itmo.calls.service.dto

import ru.itmo.calls.config.dto.ItmoIdDto

data class JwtPayloadDto(
    val isu: Int?,
    val name: String?,
    val groups: List<GroupDto>?,
    val picture: String?
) {
    data class GroupDto(
        val name: String?
    )

    fun toItmoIdDto(): ItmoIdDto? {
        val id = isu ?: return null
        val userName = name ?: return null
        val groupName = groups?.firstOrNull()?.name ?: ""
        val pictureUrl = picture ?: ""

        return ItmoIdDto(
            id = id,
            name = userName,
            groupName = groupName,
            pictureUrl = pictureUrl
        )
    }
}