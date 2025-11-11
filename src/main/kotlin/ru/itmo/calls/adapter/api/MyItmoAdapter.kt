package ru.itmo.calls.adapter.api

import api.myitmo.MyItmo
import api.myitmo.model.ResultResponse
import api.myitmo.model.personality.PersonalityMin
import org.springframework.stereotype.Service
import retrofit2.Call
import ru.itmo.calls.domain.user.UserInfo
import ru.itmo.calls.port.UserInfoProvider
import ru.itmo.calls.port.model.LoginResult

@Service
class MyItmoAdapter(
    private val myItmo: MyItmo
): UserInfoProvider {
    companion object {
        private const val USER_SEPARATOR = "|"
    }

    fun login(user: String, password: String): LoginResult {
        val result = myItmo.authHelper.auth(user, password)

        return LoginResult(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
        )
    }

    fun findUserById(id: Int): String {
        val result = myItmo.api().getPersonality(id).execute().body()!!.result
        return result.fio
    }

    override fun getUserInfo(userIds: List<Int>): List<UserInfo> {
        val filterString = userIds.joinToString(USER_SEPARATOR)
        val result = executeRequest {
            myItmo.api().searchPersonalities(userIds.size, 0, filterString)
        }

        return result?.data?.map { it.toUserInfo() } ?: emptyList()
    }

    private fun <T> executeRequest(requestProvider: () -> Call<ResultResponse<T>>): T? {
        val responseBody = requestProvider().execute().body()
        if (responseBody == null) {
            // TODO log
            return null
        }

        return responseBody.result
    }
}

private fun PersonalityMin.toUserInfo(): UserInfo {
    return UserInfo(
        userId = id,
        fullName = fio,
        photoUrl = photoUrl,
    )
}
