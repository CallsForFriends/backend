package ru.itmo.calls.port

import api.myitmo.MyItmo

interface AuthProvider {
    fun createMyItmo(): MyItmo

    fun getIdToken(username: String, password: String): String?

    fun removeMyItmo(token: String)

    fun getAuthenticatedMyItmo(): MyItmo
}
