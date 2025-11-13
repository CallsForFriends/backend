package ru.itmo.calls.service

import org.springframework.stereotype.Service
import ru.itmo.calls.config.dto.ItmoIdDto
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class TokenService {
    private val tokens: MutableMap<String, TokenInfo> = ConcurrentHashMap()

    data class TokenInfo(
        var userId: Int,
        val login: String,
        var password: String,
        val name: String,
        val groupName: String,
        val pictureUrl: String
    )

    fun generateToken(user: ItmoIdDto, login: String, password: String): String {
        val token = UUID.randomUUID().toString()
        tokens[token] = TokenInfo(user.id, login, password, user.name, user.groupName, user.pictureUrl)
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

