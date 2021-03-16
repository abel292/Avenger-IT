package com.abel.avengerit.ui.main

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.abel.avengerit.R
import com.abel.avengerit.ui.login.LoginActivity
import com.abel.avengerit.view_models.SessionViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_marvel.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private val viewModel: SessionViewModel by viewModel()
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }


    private fun init() {
        bottom_navigation.itemIconTintList = null
        navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation?.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.nav_character -> {
                if (!item.isChecked) {
                    navController.navigate(R.id.actionCharactersFragment)
                }
                true
            }
            R.id.nav_event -> {
                if (!item.isChecked) {
                    navController.navigate(R.id.actionEventsFragment)
                }
                true
            }
            else -> false
        }
    }

    fun setActionButtonToolbar(visible: Boolean) {
        if (visible) {
            imageViewExit.setImageResource(R.drawable.ic_cancel)
            imageViewExit.setOnClickListener {
                onBackPressed()
            }
        } else {
            imageViewExit.setImageResource(R.drawable.ic_logout)
            imageViewExit.setOnClickListener {
                showAlertLogout()
            }

        }
    }

    private fun showAlertLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.cerrar_session))
        builder.setPositiveButton(getString(R.string.si)) { dialog, which ->
            viewModel.logout()
            goToLogin()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, which -> }
        builder.show()
    }

    private fun goToLogin() {
        val login by inject<LoginActivity>()
        val intent = Intent(this, login::class.java)
        startActivity(intent)
        finish()
    }
}