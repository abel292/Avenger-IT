package com.abel.avengerit.model.character

import java.io.Serializable

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemComic>,
    val returned: Int
) : Serializable