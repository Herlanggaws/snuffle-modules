package com.logixmates.snuffle.core.data.web

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.logixmates.snuffle.core.data.web.validator.ResponseValidatorList
import com.logixmates.snuffle.core.domain.authtoken.GetAuthTokenUseCase
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class SnuffleHttpClient(
    private val context: Context,
    private val json: Json,
    private val getAuthTokenUseCase: GetAuthTokenUseCase,
    @Named("io") private val io: CoroutineDispatcher
) {

    val client = Ktorfit.Builder()
        .baseUrl(BASE_URL)
        .httpClient(constructHttpClient())
        .build()

    @OptIn(DelicateCoroutinesApi::class)
    private fun constructHttpClient() = HttpClient(constructOkHttpClient()) {
        install(ContentNegotiation) {
            json(json)
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, ContentType.Application.Json)
            GlobalScope.launch(io) {
                val authToken = getAuthTokenUseCase(Unit)
                if (authToken.isNotBlank()) {
                    header(
                        HttpHeaders.Authorization,
                        "Bearer ${getAuthTokenUseCase(Unit)}"
                    )
                }
            }
        }
    }

    private fun constructOkHttpClient() = OkHttp.create {
        addInterceptor(constructChuckerInterceptor())
    }

    private fun constructChuckerInterceptor() = ChuckerInterceptor.Builder(context)
        .collector(constructChuckerCollector()).maxContentLength(CHUCKER_MAX_LENGTH)
        .alwaysReadResponseBody(true)
        .createShortcut(true)
        .build()

    private fun constructChuckerCollector() = ChuckerCollector(
        context = context,
        showNotification = true,
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )

    companion object {
        private const val BASE_URL = "https://api.snuffle.app/"
        private const val CHUCKER_MAX_LENGTH = 250_000L
    }
}
