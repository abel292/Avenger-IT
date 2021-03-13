package com.abel.avengerit.model.character

import java.io.Serializable

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Any>,
    val returned: Int
) : Serializable