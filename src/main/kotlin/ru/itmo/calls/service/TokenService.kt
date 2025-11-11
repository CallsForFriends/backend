package ru.itmo.calls.service

import org.springframework.stereotype.Service
import java.util.*
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
    
    fun updateAuthInfo(token: String, refreshTokenExpiresIn: Long = 24 * 60 * 60 * 1000) { // По умолчанию 24 часа
        val tokenInfo = tokens[token]
        if (tokenInfo != null) {
            val now = System.currentTimeMillis()
            tokenInfo.lastAuthTime = now
            tokenInfo.refreshTokenExpiresAt = now + refreshTokenExpiresIn
        }
    }
    
    fun isRefreshTokenValid(token: String): Boolean {
        val tokenInfo = tokens[token] ?: return false
        val now = System.currentTimeMillis()
        return tokenInfo.refreshTokenExpiresAt != null && 
               tokenInfo.refreshTokenExpiresAt!! > now
    }
    
    /**
     * Находит существующий токен для пользователя по login
     * @return токен, если найден, иначе null
     */
    fun findTokenByLogin(login: String): String? {
        return tokens.entries.firstOrNull { it.value.login == login }?.key
    }
    
    fun cleanupExpiredTokens(maxAge: Long = 24 * 60 * 60 * 1000) { // 24 часа
        val now = System.currentTimeMillis()
        tokens.entries.removeIf { (_, info) -> now - info.createdAt > maxAge }
    }
}

