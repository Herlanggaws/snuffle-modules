package com.logixmates.snuffle.auth.data.web

import com.logixmates.snuffle.auth.data.model.LoginRequest
import com.logixmates.snuffle.auth.data.model.LoginResponse
import com.logixmates.snuffle.core.data.model.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Part

interface LoginWebApi {
    @POST("api/auth/login")
    suspend fun doLogin(
        @Body request: LoginRequest
    ): LoginResponse

    @Multipart
    @POST("api/auth/forgot-password")
    suspend fun requestForgotPassword(
        @Part("email") email: String
    ): Response
}
