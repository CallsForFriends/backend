package ru.itmo.calls.port

import api.myitmo.MyItmo
import ru.itmo.calls.domain.user.UserInfo

interface UserInfoProvider {
    fun getUserInfo(id: Int): UserInfo
    fun findUserInfoByIds(userIds: List<Int>): List<UserInfo>
    fun findUserInfoByFilter(filter: String, limit: Int, offset: Int): List<UserInfo>

    @Deprecated(message = "remove after parsing token implementation")
    fun getUserInfo(id: Int, customMyItmo: MyItmo): UserInfo
}
