package com.example.easyfood.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.easyfood.R
import com.example.easyfood.fragments.CategoriesFragment
import com.example.easyfood.fragments.FavoritesFragment
import com.example.easyfood.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.fragment_holder, HomeFragment()).commit()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btn_nav);
        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, HomeFragment())
            .commit()
//        val navigationController = Navigation.findNavController(this, R.id.host_fragment);

//        NavigationUI.setupWithNavController(bottomNavigation, navigationController);
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, HomeFragment())
                        .commit()
                    true;
                }

                R.id.fravoritesFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, FavoritesFragment())
                        .commit()
                    true;
                }

                R.id.categoriesFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, CategoriesFragment())
                        .commit()
                    true;
                }

                else -> false
            };
        }
    }
}