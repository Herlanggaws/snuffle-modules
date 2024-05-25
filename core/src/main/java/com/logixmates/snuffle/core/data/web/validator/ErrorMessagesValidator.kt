package com.logixmates.snuffle.core.data.web.validator

import com.logixmates.snuffle.core.data.model.Response
import com.logixmates.snuffle.core.data.web.exception.HttpErrorMessageException
import io.ktor.client.statement.HttpResponse

class ErrorMessagesValidator : ResponseValidator {
    override suspend fun validate(response: HttpResponse, body: Response) {
        if (!body.error.isNullOrEmpty()) {
            throw HttpErrorMessageException(body.error.first())
        }
    }
}
