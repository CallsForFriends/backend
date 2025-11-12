package ru.itmo.calls.adapter.api

import api.myitmo.MyItmo
import api.myitmo.model.ResultResponse
import api.myitmo.model.personality.Personality
import api.myitmo.model.personality.PersonalityMin
import mu.KotlinLogging
import org.springframework.beans.factory.ObjectProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import retrofit2.Call
import ru.itmo.calls.domain.exception.UserNotFoundException
import ru.itmo.calls.domain.user.UserInfo
import ru.itmo.calls.port.UserInfoProvider
import ru.itmo.calls.service.TokenService
import java.util.concurrent.ConcurrentHashMap

@Service
class MyItmoAdapter(
    private val myItmoProvider: ObjectProvider<MyItmo>,
    private val tokenService: TokenService
) : UserInfoProvider {
    companion object {
        private const val USER_SEPARATOR = "|"
    }

    private val log = KotlinLogging.logger { }
    private val myItmoCache: MutableMap<String, MyItmo> = ConcurrentHashMap()
    private val myItmo
        get() = getAuthenticatedMyItmo()

    private fun createMyItmo(): MyItmo {
        return myItmoProvider.getObject()
    }

    fun login(username: String, password: String): Boolean {
        return try {
            // Создаем новый экземпляр MyItmo для проверки
            val createdMyItmo = createMyItmo()
            // Используем auth() для проверки, как указано в требованиях
            createdMyItmo.auth(username, password)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Получает userId текущего пользователя после успешной аутентификации
     * @param username логин пользователя
     * @param password пароль пользователя
     * @return userId пользователя или null, если не удалось получить
     */
    fun getUserIdAfterLogin(username: String, password: String): Int? {
        return try {
            val loggedMyItmo = createMyItmo()
            loggedMyItmo.auth(username, password)
            // Пытаемся получить userId через поиск пользователя по логину
            // Используем searchPersonalities с фильтром по логину
            val result = executeRequest {
                loggedMyItmo.api().searchPersonalities(1, 0, username)
            }
            // Возвращаем id первого найденного пользователя (преобразуем Long в Int)
            result?.data?.firstOrNull()?.id?.toInt()
        } catch (e: Exception) {
            // Если не удалось получить через поиск, возвращаем null
            // В этом случае можно будет использовать fallback значение или другой способ
            null
        }
    }

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

        return result?.data?.map { it.toUserInfo() } ?: emptyList()    }

    private fun <T> executeRequest(requestProvider: () -> Call<ResultResponse<T>>): T? {
        val response = requestProvider().execute()
        if (!response.isSuccessful) {
            log.error("[ITMO API] Request failed with status ${response.code()}: ${response.message()}. ${response.errorBody()?.string()}")
            return null
        }

        return response.body()?.result
    }

    fun removeMyItmo(token: String) {
        myItmoCache.remove(token)
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
        val authenticatedMyItmo = myItmoCache.computeIfAbsent(token) {
            val newMyItmo = createMyItmo()
            // При первом создании вызываем auth для установки сессии
            newMyItmo.auth(tokenInfo.login, tokenInfo.password)
            newMyItmo
        }

        // Если токен есть в системе, просто возвращаем кэшированный MyItmo
        // без повторного вызова auth()
        return authenticatedMyItmo
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
