package com.rn.adapter

import com.squareup.moshi.Moshi

object MoshiProvider {
    val moshi = Moshi.Builder()
            .add(NodeNlpAdapter())
            .build()
}