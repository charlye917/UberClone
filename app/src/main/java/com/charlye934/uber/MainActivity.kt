package com.charlye934.uber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.charlye934.uber.databinding.ActivityMainBinding
import com.charlye934.uber.login.presentation.navigator.UberLoginNavigateImp
import com.charlye934.uber.login.presentation.navigator.UberLoginNavigation

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
    }

    private fun setUpToolbar(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply{
            setupWithNavController(navController)
            setTitle("UBER CLONE")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}