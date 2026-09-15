package com.example.miformacionctma.data.network

class TokenProvider {
    private var token: String? = null

    fun setToken(newToken: String) { token = newToken }
    fun getToken(): String? = token
}