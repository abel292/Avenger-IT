package com.abel.avengerit.model.character

import java.io.Serializable

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
) : Serializable