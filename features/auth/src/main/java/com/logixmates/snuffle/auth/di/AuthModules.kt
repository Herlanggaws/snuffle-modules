package com.logixmates.snuffle.auth.di

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.logixmates.snuffle.auth.data.web.createLoginWebApi
import com.logixmates.snuffle.core.di.SnuffleModules
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.factory
import org.koin.dsl.module
import org.koin.ksp.generated.module

@ComponentScan("com.logixmates.snuffle.auth")
@Module
object AuthModules : SnuffleModules {
    private val apiModules = module {
        single {
            val ktorfit: Ktorfit = get()
            ktorfit.createLoginWebApi()
        }
    }
    private val googleModules = module {
        factory {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("318695216547-01tetfil178vj946caadvgqf7otu3dsu.apps.googleusercontent.com")
                .requestEmail()
                .build()
        }
        factory { param -> GoogleSignIn.getClient(param.get(), get()) }
    }

    override fun all(): List<org.koin.core.module.Module> {
        return module + apiModules + googleModules
    }
}
