package com.rn.subscriber

import com.kursivee.rn.bridge.subscriber.Subscriber

class NlpSubscriber: Subscriber<String> {
    override var callback: (String) -> Unit = {}

    override fun process(message: String) {
        callback(message)
    }
}
