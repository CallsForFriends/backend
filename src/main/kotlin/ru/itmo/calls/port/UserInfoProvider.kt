package ru.itmo.calls.port

import ru.itmo.calls.domain.user.UserInfo

interface UserInfoProvider {
    fun getUserInfo(userIds: List<Int>): List<UserInfo>
}
