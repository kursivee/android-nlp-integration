package com.kursivee.rn.bridge.subscriber

object SubscriberManager {
    val subscribers = mutableMapOf(
        "MOCK" to MockSubscriber()
    )
}