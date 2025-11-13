package ru.itmo.calls.adapter.security

import api.myitmo.MyItmo
import mu.KotlinLogging
import org.springframework.beans.factory.ObjectProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.itmo.calls.port.AuthProvider
import ru.itmo.calls.service.TokenService
import java.util.concurrent.ConcurrentHashMap

@Service
class MyItmoAuthAdapter(
    private val myItmoProvider: ObjectProvider<MyItmo>,
    private val tokenService: TokenService
) : AuthProvider {
    private val log = KotlinLogging.logger { }
    private val myItmoCache: MutableMap<String, MyItmo> = ConcurrentHashMap()

    override fun createMyItmo(): MyItmo {
        return myItmoProvider.getObject()
    }

    override fun getIdToken(username: String, password: String): String? {
        val createdMyItmo = createMyItmo()
        createdMyItmo.auth(username, password)
        return createdMyItmo.storage?.idToken;
    }

    override fun removeMyItmo(token: String) {
        myItmoCache.remove(token)
    }

    /**
     * Получает аутентифицированный MyItmo экземпляр для текущего пользователя.
     * Если токен пользователя уже есть в системе, просто возвращает кэшированный экземпляр
     * без повторного вызова auth(). Auth вызывается только при первом создании MyItmo для токена.
     */
    override fun getAuthenticatedMyItmo(): MyItmo {
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
