package com.abel.avengerit.model.character

import java.io.Serializable

data class Thumbnail(
    val extension: String,
    val path: String
) : Serializable