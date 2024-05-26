package com.logixmates.snuffle.core.data.web.validator

import com.logixmates.snuffle.core.data.model.Response
import com.logixmates.snuffle.core.data.web.exception.HttpStatusCodeException

class StatusCodeValidator : ResponseValidator {

    override suspend fun validate(response: Response) {
        if (response.statusCode != null && response.statusCode !in MIN_CODE..MAX_CODE) {
            throw HttpStatusCodeException(response.error?.firstOrNull() ?: response.message)
        }
    }

    companion object {
        private const val MIN_CODE = 200
        private const val MAX_CODE = 299
    }
}
