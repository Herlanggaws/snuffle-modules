package com.logixmates.snuffle.core.di

import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.logixmates.snuffle.core.data.model.Response
import com.tencent.mmkv.MMKV
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.logixmates.snuffle.core")
object CoreModule : SnuffleModules {
    private val utility = module {
        single(named("io")) {
            Dispatchers.IO
        }
        single(named("main")) {
            Dispatchers.Main
        }
        single(named("default")) {
            Dispatchers.Default
        }
        single<SharedPreferences> {
            MMKV.defaultMMKV(MMKV.SINGLE_PROCESS_MODE, "snuffle-key")
        }
    }
    private val networkModules = module {
        single {
            Json {
                prettyPrint = true
                isLenient = false
                ignoreUnknownKeys = true
            }
        }
        single {
            ChuckerCollector(
                context = get(),
                showNotification = true,
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            )
        }
        single {
            ChuckerInterceptor.Builder(get())
                .collector(get()).maxContentLength(250_000L)
                .redactHeaders("Auth-Token", "Bearer")
                .alwaysReadResponseBody(true)
                .createShortcut(true)
                .build()
        }
        single {
            OkHttp.create {
                addInterceptor(get<ChuckerInterceptor>())
            }
        }
        single {
            HttpClient(get<HttpClientEngine>()) {
                install(ContentNegotiation) {
                    json(get<Json>())
                }
                HttpResponseValidator {
                    validateResponse { response ->
                        val error: Response = response.body()
                        if (!error.error.isNullOrEmpty()) {
                            throw Error(error.error.first())
                        }
                    }
                }
                install(DefaultRequest) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    header(HttpHeaders.Accept, ContentType.Application.Json)
                }
            }
        }
        single {
            Ktorfit.Builder()
                .baseUrl("https://api.snuffle.app/")
                .httpClient(get<HttpClient>()).build()
        }
    }

    override fun all(): List<org.koin.core.module.Module> {
        return this.module + networkModules + utility
    }
}
