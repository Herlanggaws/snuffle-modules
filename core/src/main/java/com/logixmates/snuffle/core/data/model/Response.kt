package com.logixmates.snuffle.core.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
open class Response(
    @SerialName("message") val message: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("error") val error: ArrayList<String>? = null,
    @SerialName("status_code") val statusCode: Int? = null,
)
