package ru.itmo.calls.port

import api.myitmo.MyItmo

interface AuthProvider {
    fun createMyItmo(): MyItmo

    fun login(username: String, password: String): Boolean

    fun removeMyItmo(token: String)

    fun getAuthenticatedMyItmo(): MyItmo
}
