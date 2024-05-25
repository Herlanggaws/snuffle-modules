package com.logixmates.snuffle.core.data.web.validator

import com.logixmates.snuffle.core.data.model.Response
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import org.koin.core.annotation.Single

@Single
class ResponseValidatorList {

    private val validators = sequenceOf(
        StatusCodeValidator(),
        ErrorMessagesValidator(),
    )

    suspend fun validate(response: HttpResponse) {
        val responseBody = response.body<Response>()
        validators.forEach {
            it.validate(response, responseBody)
        }
    }
}
