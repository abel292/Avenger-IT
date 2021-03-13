package com.abel.avengerit.model.event

import com.abel.avengerit.model.character.ItemComic

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemComic>,
    val returned: Int
)