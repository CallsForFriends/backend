package ru.itmo.calls.adapter.api

import api.myitmo.model.ResultResponse
import api.myitmo.model.personality.Personality
import api.myitmo.model.personality.PersonalityMin
import mu.KotlinLogging
import org.springframework.stereotype.Service
import retrofit2.Call
import ru.itmo.calls.adapter.security.MyItmoAuthAdapter
import ru.itmo.calls.domain.exception.UserNotFoundException
import ru.itmo.calls.domain.user.UserInfo
import ru.itmo.calls.port.UserInfoProvider

@Service
class MyItmoAdapter(
    private val myItmoAuthAdapter: MyItmoAuthAdapter
) : UserInfoProvider {
    companion object {
        private const val USER_SEPARATOR = "|"
    }

    private val log = KotlinLogging.logger { }
    private val myItmo
        get() = myItmoAuthAdapter.getAuthenticatedMyItmo()

    override fun getUserInfo(id: Int): UserInfo {
        val result = executeRequest {
            myItmo.api().getPersonality(id)
        }

        return result?.toUserInfo() ?: throw UserNotFoundException.forUserId(id)
    }

    override fun findUserInfoByIds(userIds: List<Int>): List<UserInfo> {
        return findUserInfoByFilter(
            filter = userIds.joinToString(USER_SEPARATOR),
            limit = userIds.size,
            offset = 0
        )
    }

    override fun findUserInfoByFilter(filter: String, limit: Int, offset: Int): List<UserInfo> {
        if (limit == 0) {
            return emptyList()
        }

        val result = executeRequest {
            myItmo.api().searchPersonalities(limit, offset, filter)
        }

        return result?.data?.map { it.toUserInfo() } ?: emptyList()
    }

    private fun <T> executeRequest(requestProvider: () -> Call<ResultResponse<T>>): T? {
        val response = requestProvider().execute()
        if (!response.isSuccessful) {
            log.error("[ITMO API] Request failed with status ${response.code()}: ${response.message()}. ${response.errorBody()?.string()}")
            return null
        }

        return response.body()?.result
    }
}

private fun Personality.toUserInfo(): UserInfo {
    return UserInfo(
        userId = isu,
        fullName = fio,
        photoUrl = photoUrl,
    )
}

private fun PersonalityMin.toUserInfo(): UserInfo {
    return UserInfo(
        userId = id,
        fullName = fio,
        photoUrl = photoUrl,
    )
}
