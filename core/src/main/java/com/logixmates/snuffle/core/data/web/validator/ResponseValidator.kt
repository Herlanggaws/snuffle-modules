package com.logixmates.snuffle.core.data.web.validator

import com.logixmates.snuffle.core.data.model.Response

fun interface ResponseValidator {
    suspend fun validate(response: Response)
}
