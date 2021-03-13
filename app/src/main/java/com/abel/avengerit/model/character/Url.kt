package com.abel.avengerit.model.character

import java.io.Serializable

data class Url(
    val type: String,
    val url: String
) : Serializable