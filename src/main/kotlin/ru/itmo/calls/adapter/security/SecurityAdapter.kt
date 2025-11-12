package ru.itmo.calls.adapter.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.itmo.calls.port.AuthDataProvider
import ru.itmo.calls.service.TokenService

@Service
class SecurityAdapter : AuthDataProvider {
    override fun getCurrentUserId(): Int {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || authentication.principal == null) {
            throw IllegalStateException("User is not authenticated")
        }

        return when (val principal = authentication.principal) {
            is TokenService.TokenInfo -> {
                principal.userId
            }

            else -> {
                throw IllegalStateException("Unexpected principal type: ${principal::class.java.name}")
            }
        }
    }
}
