package ru.itmo.calls.adapter.security

import org.springframework.stereotype.Service
import ru.itmo.calls.port.AuthProvider

@Service
class SecurityAdapter: AuthProvider {
    override fun getCurrentUserId(): Int {
        return 337031
        // todo: get info from context
        // return SecurityContextHolder.getContext().authentication.principal as Int
    }
}