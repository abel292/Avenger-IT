package com.abel.avengerit.repositories

import android.util.Log
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseRepository() {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String) = flow {
        val userLoggedInFirebase = suspendCancellableCoroutine<FirebaseUser?> { cont ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    when {
                        task.exception != null -> {
                            cont.cancel(null)
                            Log.e(this@FirebaseRepository.javaClass.name, task.exception.toString())

                        }
                        task.isCanceled -> {
                            cont.cancel()
                            Log.e(this@FirebaseRepository.javaClass.name, task.result.toString())
                        }
                        task.isSuccessful -> cont.resume(firebaseAuth.currentUser)
                    }
                }
        }
        emit(userLoggedInFirebase)
    }

    suspend fun registerEmail(email: String, password: String) = flow {
        val userFirebase = suspendCancellableCoroutine<FirebaseUser?> { cont ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    when {
                        task.exception != null -> cont.cancel(null)
                        task.isCanceled -> cont.cancel()
                        task.isSuccessful -> cont.resume(firebaseAuth.currentUser)
                    }
                }
        }
        emit(userFirebase)
    }

    suspend fun logInFacebook() = flow {
        val callbackManager: CallbackManager = CallbackManager.Factory.create()
        val userFirebase = suspendCancellableCoroutine<FirebaseUser?> { cont ->
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        firebaseHandleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {}
                    override fun onError(error: FacebookException) {}
                })
        }
        emit(userFirebase)
    }


    fun firebaseHandleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                when {
                    task.exception != null -> {
                        Log.e("abel", "error: ${task.exception}")
                    }
                    task.isCanceled -> {
                        Log.e("abel", "cancelado")

                    }
                    task.isSuccessful -> {
                        Log.e("abel", "sucefull: ${task.result}")
                    }
                    else -> {
                        Log.e("abel", "cancelado else")
                    }
                }
            }

    }

    fun signOut() {
        firebaseAuth.signOut()
        LoginManager.getInstance().logOut()
    }
}

