package com.example.own

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.own.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        const val YOLK_GRID_SIZE = 30
        const val LEVEL_GRID_SIZE = 10
    }

    lateinit var binding:ActivityMainBinding
    var ownwanDays = 155
    var yolkNum =0
    var level=0
    lateinit var achieveTableData:AchieveTableData
    var yolks = ArrayList<Int>()
    val levels = ArrayList<Int>()
    lateinit var yolkGridAdapter:YolkGridAdapter
    lateinit var levelGridAdapter:LevelGridAdapter
    lateinit var achieveDBHelper : OwnDBHelper
    lateinit var ownListAdapter: OwnListAdapter
    val ownList=ArrayList<OwnListData>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDB()
        initLayer()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDB() {
        // achieve data
        achieveDBHelper = OwnDBHelper(this)
        achieveTableData = achieveDBHelper.readCurRecord()

        // 현재 날짜 보다 전날이면 그다음날 부터 오늘까지 운동데이터 조회해서 운동했는지 확인
            // 날짜 이상~이하인 것들 select
            // 운동 완료된 것이 하나 이상 존재하면 노른자 증가 -> 다음 날짜 조회

        // ownwanDays 업데이트
        achieveDBHelper.updateRecord(940) // test
        achieveTableData = achieveDBHelper.readCurRecord()

        // 폰 켤 때마다 루틴 데이터 읽어서 오늘 조건에 맞는거 보여주기
        // 과거 할일 : 운동 완료 누르면 그날 루틴 데이터 최종 저장

    }


    private fun initLayer() {
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

        // achieve 영역 초기화
        initAchieveSection()

        // 캘린더 클릭 이벤트
        // 클릭 시 마다 배열 새로 할건지?
        ownList.add(OwnListData("202020",true, "팔굽혀펴기","어깨", 5,1))
        ownList.add(OwnListData("202020",true, "달리기","다리", 1,2))
        ownList.add(OwnListData("202020",true, "플랭크","코어", 3,3))

        ownListAdapter = OwnListAdapter(ownList)
        binding.apply {
            ownListRecyclerView.layoutManager =LinearLayoutManager(parent, LinearLayoutManager.VERTICAL,false)
            ownListRecyclerView.adapter = ownListAdapter
        }




    }

    private fun initAchieveSection(){
        ownwanDays = achieveTableData.ownwanDays
        level = (ownwanDays/YOLK_GRID_SIZE)
        yolkNum = ownwanDays%YOLK_GRID_SIZE

        printAchieveSection()

        initYolkArr()
        initLevelArr()

        yolkGridAdapter = YolkGridAdapter(this,yolks)
        binding.drawerNav.findViewById<GridView>(R.id.yolkGrid).adapter = yolkGridAdapter

        levelGridAdapter = LevelGridAdapter(this,levels)
        binding.drawerNav.findViewById<GridView>(R.id.levelGrid).adapter = levelGridAdapter

    }

    private fun initLevelArr() {
        for(i: Int in 0..(LEVEL_GRID_SIZE-1)){
            if(i<LEVEL_GRID_SIZE-level% LEVEL_GRID_SIZE){
                levels.add(0)
            }else{
                levels.add(1)
            }
        }
    }

    private fun initYolkArr(){
        for(i: Int in 0..(YOLK_GRID_SIZE-1)){
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
        levelView.text = "Lv."+ level.toString()

        (levelView.layoutParams as LinearLayout.LayoutParams).topMargin =
            pxToDp(100*(LEVEL_GRID_SIZE - level% LEVEL_GRID_SIZE).toFloat())

        // level grid에도 반영

        // 노른자 개수
        val yolkNumView = findViewById<TextView>(R.id.yolkNum)
        yolkNumView.text = yolkNum.toString() +"/" + YOLK_GRID_SIZE.toString()
        // yolk grid에도 반영
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar,menu)
        return true
    }

    // achieve 아이콘 클릭 시 achieve nav open
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.achieveIcon -> {
                binding.drawerLayout.openDrawer(binding.drawerNav)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun pxToDp(pixel: Float):Int{
        val density = resources.displayMetrics.density
        return ( pixel/ density).toInt()
    }
}