package com.logixmates.snuffle.auth.data.repo

import com.logixmates.snuffle.auth.data.model.LoginRequest
import com.logixmates.snuffle.auth.data.model.LoginResponse
import com.logixmates.snuffle.core.data.model.Response

interface AuthRepo {
    suspend fun doLogin(request: LoginRequest): LoginResponse
    suspend fun requestForgotPassword(email: String): Response
}