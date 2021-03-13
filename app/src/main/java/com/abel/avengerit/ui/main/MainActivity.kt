package com.abel.avengerit.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.abel.avengerit.R
import com.abel.avengerit.utils.showToast
import com.abel.avengerit.view_models.MarvelViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MarvelViewModel by viewModel()

    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initObservable()
        init()

    }

    private fun initObservable() {
        viewModel.characterLive.observe(this, {
            this.showToast(it?.toString() + " TODOS!")
        })
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
}