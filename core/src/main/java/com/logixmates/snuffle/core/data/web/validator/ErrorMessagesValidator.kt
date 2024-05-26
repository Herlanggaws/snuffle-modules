package com.logixmates.snuffle.core.data.web.validator

import com.logixmates.snuffle.core.data.model.Response
import com.logixmates.snuffle.core.data.web.exception.HttpErrorMessageException

class ErrorMessagesValidator : ResponseValidator {
    override suspend fun validate(response: Response) {
        if (!response.error.isNullOrEmpty()) {
            throw HttpErrorMessageException(response.error.firstOrNull() ?: response.message)
        }
    }
}
