package com.abel.avengerit.utils

import java.text.SimpleDateFormat

private const val formatDefaultApi = "yyyy-MM-dd HH:mm:ss"
private const val formatEnd = "dd-MMMM yyyy"

fun changeFormatDate(dateString: String): String {
    return try {
        val parser = SimpleDateFormat(formatDefaultApi)
        val formatter = SimpleDateFormat(formatEnd)
        val result = formatter.format(parser.parse(dateString)) // "dd-MMMM yyyy"
        val list = result.split("-")

        "${list[0]} de ${list[1]}"
    } catch (e: Exception) {
        ""
    }
}