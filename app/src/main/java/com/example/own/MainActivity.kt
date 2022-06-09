package com.example.own

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import android.database.sqlite.SQLiteDatabase
import android.view.MenuItem
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.example.own.Diary.DiaryTabFragment
import com.example.own.Diary.DiaryWriteFragment
import com.example.own.Workout.WorkoutFragment
import com.example.own.databinding.ActivityMainBinding
import com.example.own.databinding.FragmentCalenderBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    lateinit var dbhelper: OwnDBHelper
    lateinit var database: SQLiteDatabase
    // const
    companion object{
        const val YOLK_GRID_SIZE = 30
        const val LEVEL_GRID_SIZE = 10
    }
    // achieve members
    var ownwanDays = 155
    var yolkNum =0
    var level=0
    lateinit var achieveTableData:AchieveTableData
    var yolks = ArrayList<Int>()
    val levels = ArrayList<Int>()
    lateinit var yolkGridAdapter:YolkGridAdapter
    lateinit var levelGridAdapter:LevelGridAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbhelper= OwnDBHelper(this)
        database = dbhelper.writableDatabase
        initLayout()
    }

    private fun initLayout() {
        supportFragmentManager.beginTransaction().replace(R.id.container, CalendarFragment()).commit()
        // 하단바 리스너
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.tab_home-> supportFragmentManager.beginTransaction().replace(R.id.container, CalendarFragment()).commit()
                R.id.tab_diary->supportFragmentManager.beginTransaction().replace(R.id.container,DiaryTabFragment()).commit()
                R.id.tab_routine-> supportFragmentManager.beginTransaction().replace(R.id.container,DiaryWriteFragment()).commit()
                R.id.tab_workout-> supportFragmentManager.beginTransaction().replace(R.id.container, WorkoutFragment()).commit()
            }

            this.supportFragmentManager.popBackStackImmediate()
            true
        }
        initAchieve()
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
                if(binding.drawerLayout.isDrawerOpen(binding.drawerNav))
                    binding.drawerLayout.closeDrawer(binding.drawerNav)
                else
                    binding.drawerLayout.openDrawer(binding.drawerNav)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initAchieve(){
        achieveTableData = dbhelper.readAchieve()

        if(achieveTableData.lastUpdateDate == null)
            dbhelper.updateAchieve(0,false)
        else{
            var yolkToAdd = 0

            // 운동한 날이라면 더해야할 yolk 개수 1 증가
            while(achieveTableData.lastUpdateDate!! < CalendarFragment().getTodayGregorian()){
//                if() // 운동데이터에 이 날짜로 검색해서 운동한 날 이면 (complete 된 운동 개수가 1이상이면)
                yolkToAdd++
                achieveTableData.lastUpdateDate!!.roll(GregorianCalendar.DAY_OF_MONTH,1)
            }

            // achieve 데이터 업데이트
            dbhelper.updateAchieve(achieveTableData.ownwanDays + yolkToAdd, achieveTableData.didWorkout)
            achieveTableData = dbhelper.readAchieve()
        }

        initAchieveLayout()
    }

    /* 성취 영역 구현 ---- 추후 깔끔하게 수정하기 */
    private fun initAchieveLayout(){
        ownwanDays = achieveTableData.ownwanDays
        level = (ownwanDays/ YOLK_GRID_SIZE)
        yolkNum = ownwanDays% YOLK_GRID_SIZE

        printAchieveSection()

        initYolkArr()
        initLevelArr()

        yolkGridAdapter = YolkGridAdapter(this,yolks)
        binding!!.drawerNav.findViewById<GridView>(R.id.yolkGrid).adapter = yolkGridAdapter

        levelGridAdapter = LevelGridAdapter(this,levels)
        binding!!.drawerNav.findViewById<GridView>(R.id.levelGrid).adapter = levelGridAdapter

    }

    private fun initLevelArr() {
        for(i: Int in 0 until LEVEL_GRID_SIZE){
            if(i< LEVEL_GRID_SIZE -level% LEVEL_GRID_SIZE){
                levels.add(0)
            }else{
                levels.add(1)
            }
        }
    }

    private fun initYolkArr(){
        for(i: Int in 0 until YOLK_GRID_SIZE){
            if(i<yolkNum){
                yolks.add(1)
            }else{
                yolks.add(0)
            }
        }
    }

    // achieve 화면 출력 함수 - 함수명 바꿀 것 (텍스트 내용만 변경함)
    private fun printAchieveSection(){
        // 오늘 운동한 날짜
        val dayCnt = findViewById<TextView>(R.id.dayCnt)
        dayCnt.text = ownwanDays.toString()

        // 레벨
        val levelView = findViewById<TextView>(R.id.levelNum)
        levelView.text = "Lv.$level"

        (levelView.layoutParams as LinearLayout.LayoutParams).topMargin =
            pxToDp(100*(LEVEL_GRID_SIZE - level% LEVEL_GRID_SIZE).toFloat())

        // level grid에도 반영

        // 노른자 개수
        val yolkNumView = findViewById<TextView>(R.id.yolkNum)
        yolkNumView.text = "$yolkNum/$YOLK_GRID_SIZE"
        // yolk grid에도 반영
    }

    private fun pxToDp(pixel: Float):Int{
        val density = resources.displayMetrics.density
        return ( pixel/ density).toInt()
    }
}
