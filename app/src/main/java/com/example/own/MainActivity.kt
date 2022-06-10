package com.example.own

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import android.database.sqlite.SQLiteDatabase
import android.os.Debug
import android.util.Log
import android.view.MenuItem
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import com.example.own.DB.OwnDBHelper
import com.example.own.Diary.DiaryTabFragment
import com.example.own.Diary.DiaryWriteFragment
import com.example.own.Workout.WorkoutData
import com.example.own.Home.AchieveTableData
import com.example.own.Home.LevelGridAdapter
import com.example.own.Home.YolkGridAdapter
import com.example.own.Routine.RoutineFragment
import com.example.own.Workout.WorkoutFragment
import com.example.own.databinding.ActivityMainBinding
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
//    val viewModel:AchieveDataViewModel by viewModels()
    var yolks = ArrayList<Int>()
    val levels = ArrayList<Int>()
    lateinit var yolkGridAdapter: YolkGridAdapter
    lateinit var levelGridAdapter: LevelGridAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbhelper= OwnDBHelper(this)
        database = dbhelper.writableDatabase
        initData()
        initLayout()
    }

    private fun initData(){
        val today = CalendarFragment().getTodayGregorian()
        val workoutDataList = dbhelper.getRoutineWorkoutList(today)
        dbhelper.deleteWorkout(today)
        Log.d("wokr",workoutDataList.toString())
        for(workout in workoutDataList){
            dbhelper.insertWorkout(workout)
        }
        val workoutList = ArrayList<WorkoutData>()
        workoutList.add(WorkoutData(1,"2022-06-10","Bench Press","CHEST","_",5, 4 ,"00:30",10
            ,10,0,false,1,false))
        workoutList.add(WorkoutData(1,"2022-06-10","Sited Row","BACK","_",3, 4 ,"00:30",10
            ,10,0,false,1,false))
        workoutList.add(WorkoutData(1,"2022-06-10","Push up","ARM","hard...",2, 4 ,"00:30",10
        ,10,1,true,1,false))
        workoutList.add(WorkoutData(1,"2022-06-10","Jump","CARDIO","It was good!",10, 4 ,"00:30",10
            ,10,1,true,1,false))
        workoutList.add(WorkoutData(1,"2022-06-10","Sqaut","HIP","soso.",2, 4 ,"00:30",10
            ,10,2,true,1,false))
        workoutList.add(WorkoutData(1,"2022-06-10","Pull down","ARM","hard...",5, 4 ,"00:30",10
            ,10,3,true,1,false))
        workoutList.add(WorkoutData(1,"2022-06-11","Side kick","LEG","_",7, 4 ,"00:30",10
            ,10,0,false,1,false))
        workoutList.add(WorkoutData(1,"2022-06-11","YOGA","MENTAL","peaceful",4, 4 ,"00:30",10
            ,10,0,false,1,false))
        workoutList.add(WorkoutData(1,"2022-06-02","Push up","ARM","Feel good",6, 4 ,"00:30",10
            ,10,0,false,1,false))
        workoutList.add(WorkoutData(1,"2022-06-02","Jogging","CARDIO","hard...",1, 4 ,"00:30",10
            ,10,3,true,1,false))

//        dbhelper.updateAchieve(135,false)
//

        for (workout in workoutList){
            dbhelper.insertWorkout(workout)
        }
    }

    private fun initLayout() {
        supportFragmentManager.beginTransaction().replace(R.id.container, CalendarFragment()).commit()
        // 하단바 리스너
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.tab_home-> supportFragmentManager.beginTransaction().replace(R.id.container, CalendarFragment()).commit()
                R.id.tab_diary->supportFragmentManager.beginTransaction().replace(R.id.container,DiaryTabFragment(),"DiaryTab").commit()
                R.id.tab_routine-> supportFragmentManager.beginTransaction().replace(R.id.container,
                    RoutineFragment()
                ).commit()
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
                else{
                    binding.drawerLayout.openDrawer(binding.drawerNav)
                    achieveTableData = dbhelper.readAchieve()
                    ownwanDays = achieveTableData.ownwanDays
                    calcLevelAndYolk()
                    initAchieveLayout()

                }
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
            val today = CalendarFragment().getTodayGregorian()
            Log.d("today",today.toString())
            Log.d("lastUpdate",achieveTableData.lastUpdateDate.toString())
            val date = achieveTableData.lastUpdateDate
            while(date!! < today){
                val workoutList = dbhelper.getWorkoutList(date)
                var didWorkout=false
                for(workout in workoutList){
                    if(workout.isDone){
                        didWorkout=true
                        break
                    }
                }
                if(didWorkout) // 운동데이터에 이 날짜로 검색해서 운동한 날 이면 (complete 된 운동 개수가 1이상이면)
                    yolkToAdd++
                date!!.add(GregorianCalendar.DAY_OF_MONTH,1)
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
        calcLevelAndYolk()
        printAchieveSection()

        initYolkArr()
        initLevelArr()

        yolkGridAdapter = YolkGridAdapter(this,yolks)
        binding!!.drawerNav.findViewById<GridView>(R.id.yolkGrid).adapter = yolkGridAdapter

        levelGridAdapter = LevelGridAdapter(this,levels)
        binding!!.drawerNav.findViewById<GridView>(R.id.levelGrid).adapter = levelGridAdapter

    }

    private fun calcLevelAndYolk(){
        level = (ownwanDays/ YOLK_GRID_SIZE)
        yolkNum = ownwanDays% YOLK_GRID_SIZE
    }

    private fun initLevelArr() {
        levels.clear()
        for(i: Int in 0 until LEVEL_GRID_SIZE){
            if(i< LEVEL_GRID_SIZE -level% LEVEL_GRID_SIZE){
                levels.add(0)
            }else{
                levels.add(1)
            }
        }
    }

    private fun initYolkArr(){
        yolks.clear()
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
        Log.d("lev",level.toString())

        (levelView.layoutParams as LinearLayout.LayoutParams).topMargin =
            pxToDp(100*(LEVEL_GRID_SIZE - level% LEVEL_GRID_SIZE).toFloat())

        // level grid에도 반영

        // 노른자 개수
        val yolkNumView = findViewById<TextView>(R.id.yolkNum)
        yolkNumView.text = "$yolkNum/$YOLK_GRID_SIZE"
        Log.d("yolk",yolkNum.toString())
        // yolk grid에도 반영
    }

    private fun pxToDp(pixel: Float):Int{
        val density = resources.displayMetrics.density
        return ( pixel/ density).toInt()
    }
}
