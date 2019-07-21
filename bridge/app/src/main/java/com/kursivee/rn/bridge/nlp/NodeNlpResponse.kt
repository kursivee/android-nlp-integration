package com.kursivee.rn.bridge.nlp

data class NodeNlpResponse(
    val locale: String?,
    val localeIso2: String?,
    val language: String?,
    val utterance: String?,
    val classification: Array<Classification>?,
    val intent: String?,
    val score: Double?,
    val entities: Array<Entity>?,
    val sentiment: Sentiment?,
    val answer: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NodeNlpResponse

        if (locale != other.locale) return false
        if (localeIso2 != other.localeIso2) return false
        if (language != other.language) return false
        if (utterance != other.utterance) return false
        if (classification != null) {
            if (other.classification == null) return false
            if (!classification.contentEquals(other.classification)) return false
        } else if (other.classification != null) return false
        if (intent != other.intent) return false
        if (score != other.score) return false
        if (entities != null) {
            if (other.entities == null) return false
            if (!entities.contentEquals(other.entities)) return false
        } else if (other.entities != null) return false
        if (sentiment != other.sentiment) return false
        if (answer != other.answer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = locale?.hashCode() ?: 0
        result = 31 * result + (localeIso2?.hashCode() ?: 0)
        result = 31 * result + (language?.hashCode() ?: 0)
        result = 31 * result + (utterance?.hashCode() ?: 0)
        result = 31 * result + (classification?.contentHashCode() ?: 0)
        result = 31 * result + (intent?.hashCode() ?: 0)
        result = 31 * result + (score?.hashCode() ?: 0)
        result = 31 * result + (entities?.contentHashCode() ?: 0)
        result = 31 * result + (sentiment?.hashCode() ?: 0)
        result = 31 * result + (answer?.hashCode() ?: 0)
        return result
    }
}