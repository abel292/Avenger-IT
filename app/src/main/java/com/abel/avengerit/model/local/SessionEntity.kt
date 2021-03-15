package com.abel.avengerit.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "_SESSIONS")
class SessionEntity(
    @PrimaryKey
    val uid: String,
    val email: String,
    val active: Boolean = false
)