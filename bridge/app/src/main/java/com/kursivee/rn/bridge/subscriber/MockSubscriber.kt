package com.kursivee.rn.bridge.subscriber

class MockSubscriber: Subscriber<String> {
    override var callback: (String) -> Unit = {}

    override fun process(message: String) {
        callback(message)
    }
}