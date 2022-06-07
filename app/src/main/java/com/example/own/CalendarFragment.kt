package com.example.own

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.own.databinding.FragmentCalenderBinding
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {
    // const
    companion object{
        const val YOLK_GRID_SIZE = 30
        const val LEVEL_GRID_SIZE = 10
    }

    lateinit var mainActivity: MainActivity

    // ownList members
    lateinit var ownListAdapter: OwnListAdapter
    val ownList=ArrayList<OwnListData>()
    // 기록 DB Helper
    // 운동 DB Helper
    // 루틴 DB Helper


    // achieve members
    var binding:FragmentCalenderBinding ?=null
    var ownwanDays = 155
    var yolkNum =0
    var level=0
    lateinit var achieveTableData:AchieveTableData
    var yolks = ArrayList<Int>()
    val levels = ArrayList<Int>()
    lateinit var yolkGridAdapter:YolkGridAdapter
    lateinit var levelGridAdapter:LevelGridAdapter
    lateinit var achieveDBHelper : OwnDBHelper // 성취 DB Helper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalenderBinding.inflate(layoutInflater,container ,false)
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDB()
        initLayer()
    }

    private fun initDB() {
        // achieve data
        achieveDBHelper = OwnDBHelper(activity as MainActivity)
//        achieveTableData = achieveDBHelper.readCurRecord()
        //achieveDBHelper = OwnDBHelper(activity)
        achieveTableData = (activity as MainActivity).dbhelper.readCurRecord()

        // 현재 날짜 보다 전날이면 그다음날 부터 오늘까지 운동데이터 조회해서 운동했는지 확인
        // 날짜 이상~이하인 것들 select
        // 운동 완료된 것이 하나 이상 존재하면 노른자 증가 -> 다음 날짜 조회

        // ownwanDays 업데이트

//        (activity as MainActivity).dbhelper.updateRecord(940) // test
//        achieveTableData = (activity as MainActivity).dbhelper.readCurRecord()

        Toast.makeText(activity,achieveTableData.toString(),Toast.LENGTH_LONG).show()
//        Log.v("this", achieveTableData.ownwanDays.toString())
        // 폰 켤 때마다 루틴 데이터 읽어서 오늘 조건에 맞는거 보여주기
        // 과거 할일 : 운동 완료 누르면 그날 루틴 데이터 최종 저장

    }


    private fun initLayer() {

        // achieve 영역 초기화
        initAchieveSection()

        // ownList adapter 초기화
        initOwnList()

        // Calender 초기화 - 항상 initOwnList 보다 뒤에 와야 함
        initCalender()

    }

    private fun initCalender(){
        // calender click 이벤트

        binding!!.calendarView.isLongClickable = true;

        // 안 먹힘
//        binding.calendarView.setOnLongClickListener {
//            Toast.makeText(applicationContext, "long Click", Toast.LENGTH_SHORT).show()
//            true
//        }
        binding!!.calendarView.setOnDateChangeListener {  view, year, month, dayOfMonth->

            val today = Calendar.getInstance()
            // Toast.makeText(applicationContext, view.getChildAt(0), Toast.LENGTH_LONG).show()


//            view.focusedChild.setOnLongClickListener {
//                Toast.makeText(applicationContext, "long Click", Toast.LENGTH_SHORT).show()
//                true
//            }

            val clickedDate = GregorianCalendar(year,month,dayOfMonth)
            val todayDate = GregorianCalendar(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(
                Calendar.DAY_OF_MONTH) )

            // 과거
            if(clickedDate.before(todayDate)){
                // 운동 완료 버튼 안보이게 처리 (자동 완료 처리되기 때문)
                binding!!.completeWorkout.visibility = View.GONE

                // 운동 데이터 - 날짜에 해당하는 데이터 불러오기
                // ownList = 날짜 데이터 반환 값(리스트)
                // ownListAdapter.notifyDataSetChanged()

                // 기록 데이터 - boolean 값 반환하도록
                val didRecorded = false; // 여기다 대입
                if(didRecorded){
                    binding!!.writeDiary.visibility = View.GONE
                }else{
                    binding!!.writeDiary.visibility = View.VISIBLE
                }

                // Test
                Toast.makeText(activity,"과거", Toast.LENGTH_SHORT).show()

            }
            // 미래
            else if(clickedDate.after(todayDate)){
                // 운동 완료 & 기록 작성 버튼 안보이도록
                binding!!.apply {
                    writeDiary.visibility = View.GONE
                    completeWorkout.visibility =View.GONE
                }

                // 루틴 데이터 - 이날 요일에 해당하는 데이터 리스트 리턴 받기

            }
            // 현재
            else{
                // 운동 완료 여부와 기록 여부에 따라  - 운동 DB & 기록 DB 에서 받아오기(?)
                val didComplete = false;
                val didRecorded = false;
                binding!!.apply {
                    if(!didComplete){
                        completeWorkout.visibility = View.VISIBLE
                        writeDiary.visibility = View.GONE
                    }else if(didComplete && !didRecorded){
                        completeWorkout.visibility = View.GONE
                        writeDiary.visibility = View.VISIBLE
                    }else if (didComplete && didRecorded){
                        completeWorkout.visibility = View.GONE
                        writeDiary.visibility = View.GONE
                    }

                }
                // 루틴 데이터 - 이날 요일에 해당하는 데이터 리스트 리턴 받기
                Toast.makeText(activity,"현재", Toast.LENGTH_SHORT).show()
            }

        }



    }

    private fun initOwnList(){
        // 캘린더 클릭 이벤트
        // 클릭 시 마다 배열 새로
        ownList.add(OwnListData("202020",true, "팔굽혀펴기","어깨", 5,1))
        ownList.add(OwnListData("202020",true, "달리기","다리", 1,2))
        ownList.add(OwnListData("202020",true, "플랭크","코어", 3,3))

        ownListAdapter = OwnListAdapter(ownList)
        binding!!.apply {
            ownListRecyclerView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
            ownListRecyclerView.adapter = ownListAdapter
        }
    }

    /* 성취 영역 구현 ---- 추후 깔끔하게 수정하기 */
    private fun initAchieveSection(){
        ownwanDays = achieveTableData.ownwanDays
        level = (ownwanDays/ YOLK_GRID_SIZE)
        yolkNum = ownwanDays% YOLK_GRID_SIZE

        printAchieveSection()

        initYolkArr()
        initLevelArr()

        yolkGridAdapter = YolkGridAdapter(activity,yolks)
        binding!!.drawerNav.findViewById<GridView>(R.id.yolkGrid).adapter = yolkGridAdapter

        levelGridAdapter = LevelGridAdapter(activity,levels)
        binding!!.drawerNav.findViewById<GridView>(R.id.levelGrid).adapter = levelGridAdapter

    }

    private fun initLevelArr() {
        for(i: Int in 0..(LEVEL_GRID_SIZE -1)){
            if(i< LEVEL_GRID_SIZE -level% LEVEL_GRID_SIZE){
                levels.add(0)
            }else{
                levels.add(1)
            }
        }
    }

    private fun initYolkArr(){
        for(i: Int in 0..(YOLK_GRID_SIZE -1)){
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
        val dayCnt = mainActivity.findViewById<TextView>(R.id.dayCnt)
        dayCnt.text = ownwanDays.toString()

        // 레벨
        val levelView = mainActivity.findViewById<TextView>(R.id.levelNum)
        levelView.text = "Lv."+ level.toString()

        (levelView.layoutParams as LinearLayout.LayoutParams).topMargin =
            pxToDp(100*(LEVEL_GRID_SIZE - level% LEVEL_GRID_SIZE).toFloat())

        // level grid에도 반영

        // 노른자 개수
        val yolkNumView = mainActivity.findViewById<TextView>(R.id.yolkNum)
        yolkNumView.text = yolkNum.toString() +"/" + YOLK_GRID_SIZE.toString()
        // yolk grid에도 반영
    }



    // achieve 아이콘 클릭 시 achieve nav open
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.achieveIcon -> {
                binding!!.drawerLayout.openDrawer(binding!!.drawerNav)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun pxToDp(pixel: Float):Int{
        val density = resources.displayMetrics.density
        return ( pixel/ density).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}