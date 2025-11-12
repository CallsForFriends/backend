package ru.itmo.calls.port

import ru.itmo.calls.domain.user.UserInfo
import ru.itmo.calls.port.model.PagedCollection

interface UserInfoProvider {
    fun getUserInfo(id: Int): UserInfo
    fun findUserInfoByIds(userIds: List<Int>): List<UserInfo>
    fun findUserInfoByFilter(filter: String, limit: Int, offset: Int): PagedCollection<UserInfo>
}
