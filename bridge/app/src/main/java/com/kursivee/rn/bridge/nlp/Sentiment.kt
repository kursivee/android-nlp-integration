package com.kursivee.rn.bridge.nlp

data class Sentiment(
    val score: Double?,
    val comparative: Double?,
    val vote: String?,
    val numWords: Int?,
    val numHits: Int?,
    val type: String?,
    val language: String?
)