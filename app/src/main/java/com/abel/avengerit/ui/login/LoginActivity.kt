package com.abel.avengerit.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.abel.avengerit.R
import com.abel.avengerit.utils.showToast
import com.abel.avengerit.view_models.SessionViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: SessionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initObservable()
        initListeners()
    }

    private fun initObservable() {
        viewModel.getUserRegister().observe(this, {
            this.showToast(it?.email.toString() + " registrado!")
        })

        viewModel.getUserLoggedIn().observe(this, {
            this.showToast(it?.email.toString() + " Logueado!")
        })
    }

    private fun initListeners() {
        buttonRegistrar.setOnClickListener {
            val emailInput = editTextTextEmail.text.toString()
            val passInput = editTextTextPass.text.toString()
            viewModel.registerUserFirebase(emailInput, passInput)
        }

        buttonEntrar.setOnClickListener {
            val emailInput = editTextTextEmail.text.toString()
            val passInput = editTextTextPass.text.toString()
            viewModel.loginUserFirebase(emailInput, passInput)
        }

        buttonFacebook.setOnClickListener {
            viewModel.loginUserFacebook()
        }
    }
}