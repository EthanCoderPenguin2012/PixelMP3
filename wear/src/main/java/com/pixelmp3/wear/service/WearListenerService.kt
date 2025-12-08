package com.pixelmp3.wear.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.wearable.*

/**
 * Service to listen for communication from mobile phone
 */
class WearListenerService : WearableListenerService() {
    
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        // TODO: Handle audio file sync from phone
    }
    
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        // TODO: Handle messages from phone
    }
}
