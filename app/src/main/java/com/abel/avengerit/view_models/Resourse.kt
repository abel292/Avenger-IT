package com.abel.avengerit.view_models

import com.abel.avengerit.model.local.SessionEntity

data class Resourse<T>(
    var user: T?,
    var responseAction: Int?,
    var loading: Boolean = false
) {
    companion object {
        const val BAD = 300
        const val CANCEL = 400
        const val LOGIN_SUCCESS = 500
        const val USER_REGISTERED = 600
    }
}