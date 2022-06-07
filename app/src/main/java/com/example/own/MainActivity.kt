package com.example.own

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.own.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // 하단바 리스너
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
//                R.id.tab_home->
//                R.id.tab_record->
//                R.id.tab_routine->
//                R.id.tab_workout->

            }
            true
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar,menu)
        return true
    }

    // achieve 아이콘 클릭 시 achieve nav open
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val drawerNav = findViewById<NavigationView>(R.id.drawerNav)
        return when (item.itemId) {
            R.id.achieveIcon -> {
                drawerLayout.openDrawer(drawerNav)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
