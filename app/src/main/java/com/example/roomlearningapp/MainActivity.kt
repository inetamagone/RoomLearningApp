package com.example.roomlearningapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.roomlearningapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Change to HomeFragment
        supportFragmentManager.beginTransaction().replace(R.id.nav_container, HomeFragment())
            .commit()
        val bottomNav: BottomNavigationView = binding.bottomNavigation
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener)

    }

    // Navigation listener
    private val bottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener {
        // Switch between menu options
        when (it.itemId) {
            R.id.home -> {
                currentFragment = HomeFragment()
            }
            R.id.recycler -> {
                currentFragment = RecyclerFragment()
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.nav_container, currentFragment)
            .commit()
        true
    }
}