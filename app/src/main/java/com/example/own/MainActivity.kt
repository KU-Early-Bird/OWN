package com.example.own

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.own.Diary.DiaryTabFragment
import com.example.own.Diary.DiaryWriteFragment
import com.example.own.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.fragment_calender.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    lateinit var dbhelper: OwnDBHelper
    lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
        dbhelper= OwnDBHelper(this)
        database = dbhelper.writableDatabase
    }

    private fun initLayout() {
        // 하단바 리스너
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.tab_home-> supportFragmentManager.beginTransaction().replace(R.id.container, CalendarFragment()).commit()
                R.id.tab_diary->supportFragmentManager.beginTransaction().replace(R.id.container,DiaryTabFragment()).commit()
                R.id.tab_routine-> supportFragmentManager.beginTransaction().replace(R.id.container,DiaryWriteFragment()).commit()
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
//        // Handle item selection
        return when (item.itemId) {
            R.id.achieveIcon -> {
                drawerLayout.openDrawer(drawerNav)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
