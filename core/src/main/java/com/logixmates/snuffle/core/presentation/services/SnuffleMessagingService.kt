package com.logixmates.snuffle.core.presentation.services

import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.messaging
import com.logixmates.snuffle.core.domain.firebase.SaveFcmTokenUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class SnuffleMessagingService : FirebaseMessagingService(), KoinComponent {

    private val saveFcmTokenUseCase: SaveFcmTokenUseCase by inject()
    private val io: CoroutineDispatcher by inject(named("io"))
    private val serviceScope = CoroutineScope(io)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        serviceScope.launch {
            saveFcmTokenUseCase(token)
            Firebase.messaging.subscribeToTopic("/topics/snuffle")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
