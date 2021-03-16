package com.abel.avengerit.view_models

data class Resourse<T>(
    var resourceObject: T?,
    var responseAction: Int?,
    var message: String = "",
    var loading: Boolean = false
) {
    companion object {
        const val BAD = 300
        const val CANCEL = 400
        const val SUCCESS = 500
        const val USER_REGISTERED = 600
        const val FIELD_INVALID = 700
    }
}