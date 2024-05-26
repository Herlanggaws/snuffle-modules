package com.logixmates.snuffle.auth.data.utils

import com.logixmates.snuffle.core.data.web.validator.ResponseValidatorList
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import org.koin.java.KoinJavaComponent

suspend inline fun <reified T> JsonElement.validate(): T {
    val validators = KoinJavaComponent.get<ResponseValidatorList>(ResponseValidatorList::class.java)
    validators.validate(this)
    val json = KoinJavaComponent.get<Json>(Json::class.java)
    return json.decodeFromJsonElement(this)
}
