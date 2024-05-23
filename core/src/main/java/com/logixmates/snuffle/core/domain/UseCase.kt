package com.logixmates.snuffle.core.domain

import kotlinx.coroutines.flow.Flow
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse

abstract class UseCase<Request : Any, Response : Any> {
    abstract suspend fun invoke(request: Request): Response

    open fun stream(
        request: StoreReadRequest<Request>,
        builder: StoreBuilder<Request, Response>.() -> Unit = {}
    ): Flow<StoreReadResponse<Response>> {
        return StoreBuilder.from(
            fetcher = Fetcher.of<Request, Response> { invoke(it) }
        )
            .apply(builder)
            .build()
            .stream(request)
    }

    open fun stream(
        request: StoreReadRequest<Request>,
        sourceOfTruth: SourceOfTruth<Request, Response, Response>,
        builder: StoreBuilder<Request, Response>.() -> Unit = {}
    ): Flow<StoreReadResponse<Response>> {
        return StoreBuilder.from(
            fetcher = Fetcher.of<Request, Response> { invoke(it) },
            sourceOfTruth = sourceOfTruth
        )
            .apply(builder)
            .build()
            .stream(request)
    }
}