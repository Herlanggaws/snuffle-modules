package com.logixmates.snuffle.core.di

import android.content.SharedPreferences
import com.logixmates.snuffle.core.data.web.SnuffleHttpClient
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
            get<SnuffleHttpClient>().client
        }
    }

    override fun all(): List<org.koin.core.module.Module> {
        return this.module + networkModules + utility
    }
}
