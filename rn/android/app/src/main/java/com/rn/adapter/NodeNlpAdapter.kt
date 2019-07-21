package com.rn.adapter

import com.kursivee.rn.bridge.nlp.NodeNlpResponse
import com.squareup.moshi.FromJson

class NodeNlpAdapter {
    @FromJson
    fun fromJson(nodeNlpResponse: NodeNlpResponse): NodeNlpResponse = nodeNlpResponse
}