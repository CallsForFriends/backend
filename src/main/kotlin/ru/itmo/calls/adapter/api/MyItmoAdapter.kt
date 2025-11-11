package ru.itmo.calls.adapter.api

import api.myitmo.MyItmo
import api.myitmo.model.ResultResponse
import api.myitmo.model.personality.PersonalityMin
import org.springframework.beans.factory.ObjectProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import retrofit2.Call
import ru.itmo.calls.domain.user.UserInfo
import ru.itmo.calls.port.UserInfoProvider
import ru.itmo.calls.service.TokenService
import java.util.concurrent.ConcurrentHashMap

@Service
class MyItmoAdapter(
    private val myItmoProvider: ObjectProvider<MyItmo>,
    private val myItmo: MyItmo,
    private val tokenService: TokenService
) : UserInfoProvider {
    companion object {
        private const val USER_SEPARATOR = "|"
    }

    private val myItmoCache: MutableMap<String, MyItmo> = ConcurrentHashMap()

    private fun createMyItmo(): MyItmo {
        return myItmoProvider.getObject()
    }

    fun login(username: String, password: String): Boolean {
        return try {
            // Создаем новый экземпляр MyItmo для проверки
            val myItmo = createMyItmo()
            // Используем auth() для проверки, как указано в требованиях
            myItmo.auth(username, password)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun findUserById(id: Int): String {
        val myItmo = getAuthenticatedMyItmo()
        val result = myItmo.api().getPersonality(id).execute().body()!!.result
        return result.fio
    }

    fun removeMyItmo(token: String) {
        myItmoCache.remove(token)
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

    /**
     * Получает аутентифицированный MyItmo экземпляр для текущего пользователя.
     * Если токен пользователя уже есть в системе, просто возвращает кэшированный экземпляр
     * без повторного вызова auth(). Auth вызывается только при первом создании MyItmo для токена.
     */
    private fun getAuthenticatedMyItmo(): MyItmo {
        // Получаем токен из SecurityContext
        val authentication = SecurityContextHolder.getContext().authentication
        val token = authentication?.credentials as? String
            ?: throw IllegalStateException("Token not found in security context")

        // Проверяем, что токен есть в системе
        val tokenInfo = tokenService.getTokenInfo(token)
            ?: throw IllegalStateException("Token info not found - token is not in system")

        // Получаем или создаем MyItmo экземпляр для этого токена (кэширование)
        // При первом создании MyItmo для токена вызывается auth() через computeIfAbsent
        val myItmo = myItmoCache.computeIfAbsent(token) {
            val newMyItmo = createMyItmo()
            // При первом создании вызываем auth для установки сессии
            newMyItmo.auth(tokenInfo.login, tokenInfo.password)
            newMyItmo
        }

        // Если токен есть в системе, просто возвращаем кэшированный MyItmo
        // без повторного вызова auth()
        return myItmo
    }
}

private fun PersonalityMin.toUserInfo(): UserInfo {
    return UserInfo(
        userId = id,
        fullName = fio,
        photoUrl = photoUrl,
    )
}
