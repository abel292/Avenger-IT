package com.abel.avengerit.utils

import android.content.Context
import android.widget.Toast
import com.abel.avengerit.model.local.SessionEntity
import com.abel.avengerit.ui.characters_list.ComicItemInfo
import com.google.firebase.auth.FirebaseUser

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun FirebaseUser.toSessionEntity(): SessionEntity {
    return SessionEntity(uid, email ?: "")
}

fun String.getYearComic(): ComicItemInfo {
    val delimiter1 = "("
    val delimiter2 = ")"
    val parts = this.split(delimiter1, delimiter2, ignoreCase = true)
    return try {
        ComicItemInfo(parts[0], parts[1], parts[2])
    } catch (e: Exception) {
        ComicItemInfo(this)
    }
}