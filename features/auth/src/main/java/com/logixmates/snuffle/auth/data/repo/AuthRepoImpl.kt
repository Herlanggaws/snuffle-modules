package com.logixmates.snuffle.auth.data.repo

import com.logixmates.snuffle.auth.data.model.LoginRequest
import com.logixmates.snuffle.auth.data.model.LoginResponse
import com.logixmates.snuffle.auth.data.web.LoginWebApi
import com.logixmates.snuffle.core.data.model.Response
import org.koin.core.annotation.Single

@Single(binds = [AuthRepo::class])
class AuthRepoImpl(private val loginWebApi: LoginWebApi) : AuthRepo {
    override suspend fun doLogin(request: LoginRequest): LoginResponse {
        return loginWebApi.doLogin(request)
    }

    override suspend fun requestForgotPassword(email: String): Response {
        return loginWebApi.requestForgotPassword(email)
    }
}
