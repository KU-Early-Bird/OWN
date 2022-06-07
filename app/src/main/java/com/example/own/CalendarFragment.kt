package com.example.own

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.own.databinding.FragmentCalenderBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {
    // const
    companion object{
        const val YOLK_GRID_SIZE = 30
        const val LEVEL_GRID_SIZE = 10
    }

    // calendar
    var today:GregorianCalendar?=null

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
        binding = FragmentCalenderBinding.inflate(layoutInflater,container,false)
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        today = getTodayGregorian()
        initDB()
        initLayer()
    }

    private fun initDB() {
        // achieve data
        achieveDBHelper = OwnDBHelper(activity)
        achieveTableData = achieveDBHelper.readCurRecord()

        if(achieveTableData.lastUpdateDate == null)
            achieveDBHelper.updateRecord(0,false)
        else{
            var yolkToAdd = 0

            // 운동한 날이라면 더해야할 yolk 개수 1 증가
            while(achieveTableData.lastUpdateDate!! < today){
//                if() // 운동데이터에 이 날짜로 검색해서 운동한 날 이면 (complete 된 운동 개수가 1이상이면)
                yolkToAdd++
                achieveTableData.lastUpdateDate!!.roll(GregorianCalendar.DAY_OF_MONTH,1)
            }

            // achieve 데이터 업데이트
            achieveDBHelper.updateRecord(achieveTableData.ownwanDays + yolkToAdd, achieveTableData.didWorkout)
            achieveTableData = achieveDBHelper.readCurRecord()
        }


        // 오늘 날짜 ownList 가져오기
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

    private fun getTodayGregorian():GregorianCalendar{
        val today = Calendar.getInstance()
        return GregorianCalendar(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH) )
    }

    private fun initCalender(){
        // 기본 선택
        binding!!.apply {
            calendarView.selectedDate = CalendarDay.today()
            calendarView.selectionColor = Color.parseColor("#FFF8BB48")
            calendarView.addDecorator(CurrentDayDecorator(activity))
            calendarView.addDecorator(RecordedDateDecorator(ArrayList<CalendarDay>())) // 기록있는 날짜들만 넣기
        }


        // calender click 이벤트
//        binding!!.calendarView.isLongClickable = true;
        binding!!.calendarView.setOnDateChangedListener { _, date, selected ->
            if(today == null)
                today = getTodayGregorian()

            val clickedDate = GregorianCalendar(date.year,date.month-1,date.day)

            // 과거
            if(clickedDate.compareTo(today)<0){
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
            else if(clickedDate.compareTo(today)>0){
                // 운동 완료 & 기록 작성 버튼 안보이도록
                binding!!.apply {
                    writeDiary.visibility = View.GONE
                    completeWorkout.visibility =View.GONE
                }

                // 루틴 데이터 - 이날 요일에 해당하는 데이터 리스트 리턴 받기
                // Test
                Toast.makeText(activity,"미래", Toast.LENGTH_SHORT).show()

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

        // long Click 이벤트
        binding!!.calendarView.setOnDateLongClickListener { view, date ->
            // 꾹 누른 날 선택되도록
            view.selectedDate = date

            // 만약 기록 있다면
            // if(has diary)

        }



    }

    private fun initOwnList(){
        // 클릭 시 마다 배열 새로
        ownList.add(OwnListData(GregorianCalendar(2022,6,5),false, "팔굽혀펴기","어깨", 5,1))
        ownList.add(OwnListData(GregorianCalendar(2022,6,5),true, "플랭크","코어", 3,3))
        ownList.add(OwnListData(GregorianCalendar(2022,6,7),true, "윗몸일으키기","코어", 3,3))
        ownList.add(OwnListData(GregorianCalendar(2022,6,7),true, "달리기","다리", 1,2))

        // 오늘 운동 가져오기
        ownListAdapter = OwnListAdapter(ownList,today)
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
        val dayCnt = mainActivity.findViewById<TextView>(R.id.dayCnt)
        dayCnt.text = ownwanDays.toString()

        // 레벨
        val levelView = mainActivity.findViewById<TextView>(R.id.levelNum)
        levelView.text = "Lv.$level"

        (levelView.layoutParams as LinearLayout.LayoutParams).topMargin =
            pxToDp(100*(LEVEL_GRID_SIZE - level% LEVEL_GRID_SIZE).toFloat())

        // level grid에도 반영

        // 노른자 개수
        val yolkNumView = mainActivity.findViewById<TextView>(R.id.yolkNum)
        yolkNumView.text = "$yolkNum/$YOLK_GRID_SIZE"
        // yolk grid에도 반영
    }

    private fun pxToDp(pixel: Float):Int{
        val density = resources.displayMetrics.density
        return ( pixel/ density).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}