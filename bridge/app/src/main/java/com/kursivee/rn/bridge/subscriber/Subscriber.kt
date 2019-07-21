package com.kursivee.rn.bridge.subscriber

interface Subscriber<T> {
    var callback: (T) -> Unit
    fun process(message: String)
}