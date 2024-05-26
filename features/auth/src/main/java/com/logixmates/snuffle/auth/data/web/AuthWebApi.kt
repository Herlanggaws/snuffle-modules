package com.logixmates.snuffle.auth.data.web

import com.logixmates.snuffle.auth.data.model.LoginRequest
import com.logixmates.snuffle.auth.data.model.RegisterRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Part
import kotlinx.serialization.json.JsonElement

interface AuthWebApi {
    @POST("api/auth/login")
    suspend fun doLogin(
        @Body request: LoginRequest
    ): JsonElement

    @Multipart
    @POST("api/auth/forgot-password")
    suspend fun requestForgotPassword(
        @Part("email") email: String
    ): JsonElement

    @Multipart
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): JsonElement
}
