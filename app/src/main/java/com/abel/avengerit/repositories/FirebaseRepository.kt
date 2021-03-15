package com.abel.avengerit.repositories

import com.abel.avengerit.model.local.AppDatabase
import com.abel.avengerit.model.local.SessionEntity
import com.abel.avengerit.utils.toSessionEntity
import com.abel.avengerit.utils.valideLogin
import com.abel.avengerit.utils.valideRegister
import com.abel.avengerit.view_models.Resourse
import com.abel.avengerit.view_models.Resourse.Companion.BAD
import com.abel.avengerit.view_models.Resourse.Companion.CANCEL
import com.abel.avengerit.view_models.Resourse.Companion.FIELD_INVALID
import com.abel.avengerit.view_models.Resourse.Companion.SUCCESS
import com.abel.avengerit.view_models.Resourse.Companion.USER_REGISTERED
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseRepository(
    private val database: AppDatabase,
    private val resourse: Resourse<SessionEntity>
) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun login(email: String?, password: String?) = flow {
        if (valideLogin(email, email)) {
            suspendCancellableCoroutine<FirebaseUser?> { cont ->
                if (email != null && password != null) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            when {
                                task.exception != null -> {
                                    resourse.responseAction = BAD
                                    cont.cancel(null)
                                }
                                task.isCanceled -> {
                                    resourse.responseAction = CANCEL
                                    cont.cancel()
                                }
                                task.isSuccessful -> {
                                    resourse.responseAction = SUCCESS
                                    resourse.resourceObject = firebaseAuth.currentUser?.toSessionEntity()
                                }
                                else -> {
                                    resourse.responseAction = BAD
                                }
                            }
                            cont.resume(firebaseAuth.currentUser)
                        }
                }
            }
            if (resourse.responseAction == SUCCESS) {
                resourse.resourceObject?.let { saveSessionLocal(it) }
            }
        } else {
            resourse.responseAction = FIELD_INVALID
        }

        emit(resourse)

    }

    suspend fun registerEmail(email: String?, password: String?, passwordRepeat: String?) = flow {
        if (valideRegister(email, password, passwordRepeat)) {
            suspendCancellableCoroutine<FirebaseUser?> { cont ->
                if (email != null && password != null) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            when {
                                task.exception != null -> {
                                    resourse.responseAction = BAD
                                }
                                task.isCanceled -> {
                                    resourse.responseAction = CANCEL
                                }
                                task.isSuccessful -> {
                                    resourse.responseAction = USER_REGISTERED
                                    resourse.resourceObject = firebaseAuth.currentUser?.toSessionEntity()
                                    resourse.resourceObject?.let { saveSessionLocal(it) }
                                }
                                else -> {
                                    resourse.responseAction = BAD
                                }
                            }
                            cont.resume(firebaseAuth.currentUser)
                        }

                }
            }
        } else {
            resourse.responseAction = FIELD_INVALID
        }
        emit(resourse)

    }

    suspend fun logInFacebook(token: AccessToken) = flow {
        val credential = FacebookAuthProvider.getCredential(token.token)
        suspendCancellableCoroutine<AuthResult?> { cont ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    when {
                        task.exception != null -> {
                            resourse.responseAction = BAD
                            cont.cancel(null)
                        }
                        task.isCanceled -> {
                            resourse.responseAction = CANCEL
                            cont.cancel()
                        }
                        task.isSuccessful -> {
                            resourse.responseAction = SUCCESS
                            resourse.resourceObject = firebaseAuth.currentUser?.toSessionEntity()
                            resourse.resourceObject?.let { saveSessionLocal(it) }
                        }
                        else -> {
                            resourse.responseAction = BAD
                        }
                    }
                    cont.resume(task.result)
                }
        }
        emit(resourse)
    }

    suspend fun sessionActive() = flow {
        val userActive = database.sessionDao().getAll()
        if (!userActive.isNullOrEmpty()) {
            resourse.responseAction = SUCCESS
            emit(resourse)
        }
    }

    private fun saveSessionLocal(sessionEntity: SessionEntity) {
        GlobalScope.launch {
            database.sessionDao().insert(sessionEntity)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        LoginManager.getInstance().logOut()
    }
}

