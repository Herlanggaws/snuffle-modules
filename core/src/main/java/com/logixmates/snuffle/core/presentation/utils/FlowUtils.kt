package com.logixmates.snuffle.core.presentation.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce

private const val DEFAULT_DEBOUNCES = 250L

fun <T : Any> Flow<T>.debounceOnEvent(
    timeoutMillis: Long = DEFAULT_DEBOUNCES,
    vararg events: Any
): Flow<T> {
    return debounce { if (it in events) timeoutMillis else 0L }
}

fun <T : Any> Flow<T>.debounceOnEvent(vararg events: Any): Flow<T> {
    return debounce { if (it in events) DEFAULT_DEBOUNCES else 0L }
}
