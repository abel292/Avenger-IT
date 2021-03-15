package com.abel.avengerit.utils

import android.content.Context
import android.widget.Toast
import com.abel.avengerit.model.local.SessionEntity
import com.google.firebase.auth.FirebaseUser

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun FirebaseUser.toSessionEntity(): SessionEntity {
    return SessionEntity(uid, email ?: "")
}