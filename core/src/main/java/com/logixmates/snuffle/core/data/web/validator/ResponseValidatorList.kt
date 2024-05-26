package com.logixmates.snuffle.core.data.web.validator

import com.logixmates.snuffle.core.data.model.Response
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import org.koin.core.annotation.Single

@Single
class ResponseValidatorList(private val json: Json) {

    private val validators = sequenceOf(
        StatusCodeValidator(),
        ErrorMessagesValidator(),
    )

    suspend fun validate(response: JsonElement) {
        validators.forEach {
            val body = json.decodeFromJsonElement<Response>(response)
            it.validate(body)
        }
    }
}
