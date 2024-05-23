package com.logixmates.snuffle.core.presentation

import android.os.Parcelable
import cafe.adriel.voyager.core.registry.ScreenProvider
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
sealed class SnuffleScreens : Parcelable, ScreenProvider {
    sealed class Auth : SnuffleScreens() {
        @Parcelize
        data object Login : Auth()
    }
}
