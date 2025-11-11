package ru.itmo.calls.adapter

import api.myitmo.MyItmo
import org.springframework.stereotype.Service
import ru.itmo.calls.port.AuthProvider
import ru.itmo.calls.port.model.LoginResult

@Service
class ItmoAdapter(
    private val myItmo: MyItmo
): AuthProvider {
    fun login(user: String, password: String): LoginResult {
        val result = myItmo.authHelper.auth(user, password)

        return LoginResult(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
        )
    }

    fun findUserById(id: Int): String {
        val result = myItmo.api().getPersonality(id).execute().body()!!.result
        return result.fio
    }
}
