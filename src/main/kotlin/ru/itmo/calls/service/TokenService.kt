package ru.itmo.calls.service

import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class TokenService {
    private val tokens: MutableMap<String, TokenInfo> = ConcurrentHashMap()

    data class TokenInfo(
        val userId: String,
        val login: String,
        var password: String, // var для возможности обновления пароля
        val createdAt: Long = System.currentTimeMillis(),
        var lastAuthTime: Long? = null,
        var refreshTokenExpiresAt: Long? = null
    )

    fun generateToken(userId: String, login: String, password: String): String {
        val token = UUID.randomUUID().toString()
        tokens[token] = TokenInfo(userId, login, password)
        return token
    }

    fun getTokenInfo(token: String?): TokenInfo? {
        return validateToken(token)
    }

    fun validateToken(token: String?): TokenInfo? {
        if (token == null) return null
        return tokens[token]
    }

    fun removeToken(token: String) {
        tokens.remove(token)
    }

    /**
     * Находит существующий токен для пользователя по login
     * @return токен, если найден, иначе null
     */
    fun findTokenByLogin(login: String): String? {
        return tokens.entries.firstOrNull { it.value.login == login }?.key
    }
}

