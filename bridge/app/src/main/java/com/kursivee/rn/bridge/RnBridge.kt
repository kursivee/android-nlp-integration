package com.kursivee.rn.bridge

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.kursivee.rn.bridge.subscriber.SubscriberManager

class RnBridge(reactApplicationContext: ReactApplicationContext):
    ReactContextBaseJavaModule(reactApplicationContext) {

    override fun getName(): String = "RnBridge"

    @ReactMethod
    fun publish(eventType: String, message: String) {
        SubscriberManager.subscribers.get(eventType)?.process(message)
    }
}