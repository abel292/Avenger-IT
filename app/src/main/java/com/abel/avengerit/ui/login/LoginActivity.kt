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
import com.abel.avengerit.utils.setClipboard
import com.abel.avengerit.view_models.Resourse.Companion.BAD
import com.abel.avengerit.view_models.Resourse.Companion.FIELD_INVALID
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import com.abel.avengerit.view_models.Resourse.Companion.SUCCESS
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
        init()
        viewModel.isUserInLog()
    }

    private fun init() {
        editTextTextEmail.clearFocus()
        editTextTextPass.clearFocus()
        editTextTextPassRepeat.clearFocus()
    }

    private fun initObservable() {

        viewModel.getResourceLive().observe(this, {
            when (it.responseAction) {
                SUCCESS -> goToMain()
                BAD -> baseContext.showToast(it.message)
                FIELD_INVALID -> baseContext.showToast(getString(R.string.completar_campos))
                USER_REGISTERED -> {
                    baseContext.showToast(getString(R.string.registered_user_succes))
                    buttonRegistrar.performClick()
                    viewModeLogin()
                }
            }
        })
    }

    private fun initListeners() {
        buttonEntrar.setOnClickListener {
            val emailInput = editTextTextEmail.text.toString().trim()
            val passInput = editTextTextPass.text.toString().trim()
            viewModel.loginUserFirebase(emailInput, passInput)
        }

        buttonRegistrarUser.setOnClickListener {
            val emailInput = editTextTextEmail.text.toString().trim()
            val passInput = editTextTextPass.text.toString().trim()
            val passInputRepeat = editTextTextPassRepeat.text.toString().trim()
            viewModel.registerUserFirebase(emailInput, passInput, passInputRepeat)
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

        imageViewClipBoard.setOnClickListener {
            setClipboard(baseContext, "sharon_ijgjxre_martina@tfbnw.net") //contraseña: prueba1234
            baseContext.showToast("Email copiado en ! \nLa contraseña es: prueba1234")
            Log.e("click", "imageViewClipBoard?.text.toString()")
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

    private fun viewModeLogin() {
        buttonRegistrar.text = getString(R.string.registrarse)
        textInputLayouPassRepeat.visibility = View.GONE
        buttonEntrar.text = getString(R.string.entrar)

    }
}