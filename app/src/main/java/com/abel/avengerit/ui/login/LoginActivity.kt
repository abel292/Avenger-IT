package com.abel.avengerit.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.abel.avengerit.ui.main.MainActivity
import com.abel.avengerit.utils.showToast
import com.abel.avengerit.view_models.SessionViewModel
import com.facebook.*
import com.abel.avengerit.R
import com.abel.avengerit.view_models.Resourse.Companion.BAD
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import com.abel.avengerit.view_models.Resourse.Companion.LOGIN_SUCCESS
import com.abel.avengerit.view_models.Resourse.Companion.USER_REGISTERED


class LoginActivity : AppCompatActivity() {

    var callbackManager: CallbackManager? = null
    private val viewModel: SessionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create()

        initObservable()
        initListeners()
        viewModel.isUserInLog()
        container_mayor.requestFocus()
    }

    private fun initObservable() {

        viewModel.getResourceLive().observe(this, {
            when (it.responseAction) {
                LOGIN_SUCCESS -> goToMain()
                BAD -> baseContext.showToast(getString(R.string.error_login))
                USER_REGISTERED -> {
                    baseContext.showToast(getString(R.string.registered_user_succes))
                    viewModeLogin()
                }
            }
        })
    }

    private fun initListeners() {
        buttonEntrar.setOnClickListener {
            val emailInput = editTextTextEmail.text.toString().trim()
            val passInput = editTextTextPass.text.toString().trim()
            if (textInputLayouPassRepeat.visibility == View.VISIBLE) {
                viewModel.registerUserFirebase(emailInput, passInput)
            } else {
                viewModel.loginUserFirebase(emailInput, passInput)
            }
        }

        buttonFacebook.setOnClickListener {
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {
                        Log.i(this@LoginActivity.javaClass.name, "facebook:onCancel")
                    }

                    override fun onError(error: FacebookException) {
                        Log.i(this@LoginActivity.javaClass.name, "facebook:onError", error)
                    }
                })
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"));

        }

        buttonRegistrar.setOnClickListener {
            if (textInputLayouPassRepeat.visibility != View.VISIBLE) {
                viewModeRegister()
            } else {
                viewModeLogin()
            }
        }
    }

    private fun goToMain() {
        val mainActivity by inject<MainActivity>()
        val intent = Intent(this, mainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        viewModel.loginUserFacebook(token)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun viewModeRegister() {
        buttonRegistrar.text = getString(R.string.ya_tengo_una)
        textInputLayouPassRepeat.visibility = View.VISIBLE
    }

    private fun viewModeLogin() {
        buttonRegistrar.text = getString(R.string.registrarse)
        textInputLayouPassRepeat.visibility = View.GONE
    }
}