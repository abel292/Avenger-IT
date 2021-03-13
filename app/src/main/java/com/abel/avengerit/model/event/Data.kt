package com.abel.avengerit.model.event

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Event>,
    val total: Int
)