package ru.itmo.calls.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import ru.itmo.calls.config.dto.ItmoIdDto
import ru.itmo.calls.service.dto.JwtPayloadDto
import java.util.Base64

@Service
class JwtParsingService(
    private val objectMapper: ObjectMapper
) {
    /**
     * Парсит JWT токен и извлекает информацию о пользователе
     * @param jwtToken JWT токен в формате header.payload.signature
     * @return ItmoIdDto с информацией о пользователе или null, если не удалось распарсить
     */
    fun parseItmoIdFromJwt(jwtToken: String): ItmoIdDto? {
        val parts = jwtToken.split('.')
        if (parts.size != 3) {
            return null
        }

        val payload = parts[1]
        val decodedPayload = String(Base64.getUrlDecoder().decode(payload))

        val jwtPayload = objectMapper.readValue(decodedPayload, JwtPayloadDto::class.java)

        return jwtPayload.toItmoIdDto()
    }
}

