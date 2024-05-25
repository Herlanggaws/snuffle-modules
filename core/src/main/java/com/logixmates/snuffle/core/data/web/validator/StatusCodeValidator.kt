package com.logixmates.snuffle.core.data.web.validator

import com.logixmates.snuffle.core.data.model.Response
import com.logixmates.snuffle.core.data.web.exception.HttpStatusCodeException
import io.ktor.client.statement.HttpResponse

class StatusCodeValidator : ResponseValidator {
    override suspend fun validate(response: HttpResponse, body: Response) {
        if ((response.status.value !in MIN_CODE..MAX_CODE) ||
            (body.statusCode != null && body.statusCode !in MIN_CODE..MAX_CODE)
        ) {
            throw HttpStatusCodeException(body.message)
        }
    }

    companion object {
        private const val MIN_CODE = 200
        private const val MAX_CODE = 299
    }
}
