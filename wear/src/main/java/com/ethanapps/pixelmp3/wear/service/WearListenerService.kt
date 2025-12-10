package com.ethanapps.pixelmp3.wear.service

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
