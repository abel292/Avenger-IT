package com.abel.avengerit.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.abel.avengerit.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initListener()

    }

    private fun init() {
        bottom_navigation.itemIconTintList = null
        navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation?.setOnNavigationItemSelectedListener(this)

    }

    private fun initListener() {
        imageViewExit.setOnClickListener {
            onBackPressed()
        }
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

    fun setIconExit(visible: Boolean) {
        if (visible)
            imageViewExit.visibility = View.VISIBLE
        else
            imageViewExit.visibility = View.GONE
    }
}