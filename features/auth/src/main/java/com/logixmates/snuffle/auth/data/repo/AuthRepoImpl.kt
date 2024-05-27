package com.logixmates.snuffle.auth.data.repo

import com.logixmates.snuffle.auth.data.model.LoginRequest
import com.logixmates.snuffle.auth.data.model.LoginResponse
import com.logixmates.snuffle.auth.data.model.RegisterRequest
import com.logixmates.snuffle.core.data.utils.validate
import com.logixmates.snuffle.auth.data.web.AuthWebApi
import com.logixmates.snuffle.core.data.model.Response
import org.koin.core.annotation.Single

@Single(binds = [AuthRepo::class])
class AuthRepoImpl(private val authWebApi: AuthWebApi) : AuthRepo {

    override suspend fun doLogin(request: LoginRequest): LoginResponse {
        val response = authWebApi.doLogin(request)
        return response.validate()
    }

    override suspend fun register(request: RegisterRequest): LoginResponse {
        val response = authWebApi.register(request)
        return response.validate()
    }

    override suspend fun requestForgotPassword(email: String): Response {
        val response = authWebApi.requestForgotPassword(email)
        return response.validate()
    }
}
