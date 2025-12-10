package com.ethanapps.pixelmp3.mobile.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.wearable.*

/**
 * Service to handle communication with Wear OS devices
 */
class WearDataService : Service(), DataClient.OnDataChangedListener, MessageClient.OnMessageReceivedListener {
    
    override fun onCreate() {
        super.onCreate()
        // TODO: Initialize Wearable API
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        // TODO: Handle data changes from wear device
    }
    
    override fun onMessageReceived(messageEvent: MessageEvent) {
        // TODO: Handle messages from wear device
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // TODO: Clean up
    }
}
