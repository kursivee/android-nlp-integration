package com.rn.list

data class ChatMessage(
    val message: String,
    val isResponse: Boolean = false
)