package com.example.myapplication

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity()  {
    lateinit var binding: ActivityMainBinding
    lateinit var dbhelper: MyDBManager
    lateinit var database :SQLiteDatabase
    val textarr= arrayListOf<String>("이미지", "리스트")
    val iconarr = arrayListOf<Int>(R.drawable.ic_baseline_local_airport_24,R.drawable.ic_baseline_local_pizza_24)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()

        dbhelper=MyDBManager(this)
        database=dbhelper.writableDatabase
    }

    private fun initLayout() {
        binding.viewpager.adapter = MyViewPagerAdapter(this)//fragmentactivity들어가야하므로
        TabLayoutMediator(binding.tablayout, binding.viewpager){
            tab, position->
            tab.text = textarr[position]
            tab.setIcon(iconarr[position])
        }.attach() // 어댑터 다 설정 잘해야 attach
    }
}
