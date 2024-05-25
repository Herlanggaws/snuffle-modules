package com.logixmates.snuffle.core.data.web.validator

import com.logixmates.snuffle.core.data.model.Response
import io.ktor.client.statement.HttpResponse

fun interface ResponseValidator {
    suspend fun validate(response: HttpResponse, body: Response)
}
