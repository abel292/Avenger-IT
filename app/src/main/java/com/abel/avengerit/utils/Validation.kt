package com.abel.avengerit.utils

import java.util.regex.Pattern

fun valideLogin(email: String?, pass: String?): Boolean {
    return !email.isNullOrEmpty() && isValidEmailAddress(email) && !pass.isNullOrEmpty()
}

fun valideRegister(email: String?, pass: String?, passRepeat: String?): Boolean {
    return valideLogin(email, pass) && pass.equals(passRepeat)
}

private fun isValidEmailAddress(email: String?): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}