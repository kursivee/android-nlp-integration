package com.kursivee.rn.bridge.nlp

data class Entity(
    val start: Int?,
    val end: Int?,
    val len: Int?,
    val accuracy: Double?,
    val sourceTest: String?,
    val utteranceText: String?,
    val entity: String?,
    val resolution: Resolution?
)