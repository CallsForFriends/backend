package ru.itmo.calls.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.itmo.calls.service.TokenService

@Component
class TokenAuthenticationFilter(
    private val tokenService: TokenService
) : OncePerRequestFilter() {

    companion object {
        private const val TOKEN_COOKIE_NAME = "AUTH_TOKEN"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = extractToken(request)
        if (token != null) {
            val tokenInfo = tokenService.validateToken(token)
            if (tokenInfo != null) {
                val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                val authentication = UsernamePasswordAuthenticationToken(
                    tokenInfo,
                    token,
                    authorities
                ).apply {
                    details = WebAuthenticationDetailsSource().buildDetails(request)
                }
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }

        // Затем проверяем cookie
        val cookies = request.cookies
        if (cookies != null) {
            val cookie = cookies.firstOrNull { it.name == TOKEN_COOKIE_NAME }
            if (cookie != null && cookie.value.isNotEmpty()) {
                return cookie.value
            }
        }

        return null
    }
}
