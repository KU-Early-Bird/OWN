package com.example.own

<<<<<<< Updated upstream
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
=======
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.own.Diary.DiaryTabFragment
import com.example.own.Diary.DiaryWriteFragment
>>>>>>> Stashed changes
import com.example.own.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
<<<<<<< Updated upstream
=======
    lateinit var dbhelper: OwnDBHelper
    lateinit var database: SQLiteDatabase
>>>>>>> Stashed changes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
<<<<<<< Updated upstream
       // 하단바 리스너
=======

        initlayout()
        dbhelper= OwnDBHelper(this)
        database = dbhelper.writableDatabase
    }

    private fun initlayout() {
        // 하단바 리스너
>>>>>>> Stashed changes
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.tab_home->getSupportFragmentManager().beginTransaction().replace(R.id.container, CalendarFragment()).commit()
                R.id.tab_diary->getSupportFragmentManager().beginTransaction().replace(R.id.container,DiaryTabFragment()).commit()
                R.id.tab_routine->getSupportFragmentManager().beginTransaction().replace(R.id.container,DiaryWriteFragment()).commit()
//                R.id.tab_workout->

            }
            true
        }
    }
<<<<<<< Updated upstream
=======


>>>>>>> Stashed changes
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar,menu)
        return true
    }
}
