package com.logixmates.snuffle.auth.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.logixmates.snuffle.auth.data.model.LoginResponse
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Keep
data class LoginDomain(
    val name: String
) : Parcelable {
    constructor(response: LoginResponse?) : this(
        name = response?.data?.name.orEmpty()
    )
}
