package ru.itmo.calls.port.model

data class CurrentUserInfo(
    var userId: Int,
    val name: String,
    val groupName: String,
    val pictureUrl: String
)
