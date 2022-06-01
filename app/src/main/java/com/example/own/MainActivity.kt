package com.example.own

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import com.example.own.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    var ownwanDays = 155
    var yolkNum =0
    var level=0
    val YOLK_GRID_SIZE = 30
    val LEVEL_GRID_SIZE = 10
    var yolks = ArrayList<Int>()
    val levels = ArrayList<Int>()
    lateinit var yolkGridAdapter:YolkGridAdapter
    lateinit var levelGridAdapter:LevelGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayer()
    }

    private fun initLayer() {
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
            if(i<LEVEL_GRID_SIZE-level){
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

        (levelView.layoutParams as LinearLayout.LayoutParams).topMargin =pxToDp(100*(LEVEL_GRID_SIZE - level).toFloat())

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