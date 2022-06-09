package com.example.own

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.own.Diary.DiaryData
import com.example.own.Diary.DiaryWriteFragment
import com.example.own.databinding.FragmentCalenderBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
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
    var clickedDate:GregorianCalendar?=null
    val dateFomatter = SimpleDateFormat("yyyy-MM-dd")

    lateinit var mainActivity: MainActivity

    // ownList members
    lateinit var ownListAdapter: OwnListAdapter
    val ownList=ArrayList<OwnListData>()
    var diaryData:DiaryData?=null

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
    lateinit var ownDBHelper : OwnDBHelper // 성취 DB Helper

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
        clickedDate = getTodayGregorian()

        initDB()
        initLayer()
    }

    private fun initDB() {
        // achieve data
        ownDBHelper = OwnDBHelper(activity as MainActivity)
        achieveTableData = (activity as MainActivity).dbhelper.readAchieve()

        if(achieveTableData.lastUpdateDate == null)
            ownDBHelper.updateAchieve(0,false)
        else{
            var yolkToAdd = 0

            // 운동한 날이라면 더해야할 yolk 개수 1 증가
            while(achieveTableData.lastUpdateDate!! < today){
//                if() // 운동데이터에 이 날짜로 검색해서 운동한 날 이면 (complete 된 운동 개수가 1이상이면)
                yolkToAdd++
                achieveTableData.lastUpdateDate!!.roll(GregorianCalendar.DAY_OF_MONTH,1)
            }

            // achieve 데이터 업데이트
            ownDBHelper.updateAchieve(achieveTableData.ownwanDays + yolkToAdd, achieveTableData.didWorkout)
            achieveTableData = ownDBHelper.readAchieve()
        }


        // 오늘 날짜 ownList 가져오기
        // 폰 켤 때마다 루틴 데이터 읽어서 오늘 조건에 맞는거 보여주기
        // 과거 할일 : 운동 완료 누르면 그날 루틴 데이터 최종 저장

        // 오늘 기록 했는지 저장
        diaryData =ownDBHelper.getDiaryData(CalendarDay.today())
//        Log.d("didRecord",didRecord.toString())

    }


    private fun initLayer() {

        // achieve 영역 초기화
        initAchieveSection()

        // ownList adapter 초기화
        initOwnList()

        // Calender 초기화 - 항상 initOwnList 보다 뒤에 와야 함
        initCalender()

        // btn 초기화
        initButton()

        // 오늘 날짜에 맞춰 화면 구성 
        initTodayLayout()

    }

    private fun initButton() {
        binding!!.apply {
            // 완료 상태에 따라 버튼 다르게 보이기
            if(!achieveTableData.didWorkout){
                writeDiary.visibility = View.VISIBLE
                completeWorkout.visibility= View.GONE
            }else if(achieveTableData.didWorkout && diaryData!=null){
                writeDiary.visibility = View.GONE
                completeWorkout.visibility= View.VISIBLE
            }else if(achieveTableData.didWorkout && diaryData!=null) {
                writeDiary.visibility = View.GONE
                completeWorkout.visibility = View.GONE
            }

            writeDiary.setOnClickListener{
                //bundled data will be sent across fragment
                val date = dateFomatter.format(clickedDate!!.time)
                var bundle = Bundle()
                bundle.putString("date", date)
                setFragmentResult("DiaryWrite", bundle)

                parentFragmentManager.beginTransaction().apply{
                    replace(R.id.container, DiaryWriteFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        }
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
        binding!!.calendarView.setOnDateChangedListener { _, date, selected ->
            if(today == null)
                today = getTodayGregorian()

            clickedDate = GregorianCalendar(date.year,date.month-1,date.day)
            diaryData = ownDBHelper.getDiaryData(clickedDate!!); // 기록 데이터 존재여부 확인


            // 과거
            if(clickedDate!!.compareTo(today)<0){
                // 운동 완료 버튼 안보이게 처리 (자동 완료 처리되기 때문)
                binding!!.completeWorkout.visibility = View.GONE

                // 운동 데이터 - 날짜에 해당하는 데이터 불러오기
                // ownList = 날짜 데이터 반환 값(리스트)
                // ownListAdapter.notifyDataSetChanged()
                

                if(diaryData!=null){
                    binding!!.writeDiary.visibility = View.GONE
                }else{
                    binding!!.writeDiary.visibility = View.VISIBLE
                }

                // Test
                Toast.makeText(activity,"과거", Toast.LENGTH_SHORT).show()

            }
            // 미래
            else if(clickedDate!!.compareTo(today)>0){
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
                initTodayLayout()
            }

        }

        // long Click 이벤트
        binding!!.calendarView.setOnDateLongClickListener { view, date ->
            // 꾹 누른 날 선택되도록
            view.selectedDate = date
            diaryData = ownDBHelper.getDiaryData(date); // 기록 데이터 존재여부 확인

            // 만약 기록 있다면
            if(diaryData!=null){
                DiaryDialog(diaryData!!)
                    .show(parentFragmentManager,"DiaryDlg")
            }


        }

    }

    private fun initTodayLayout(){
        // 운동 완료 여부와 기록 여부에 따라  - 운동 DB & 기록 DB 에서 받아오기(?)
        val didComplete = false;

        binding!!.apply {
            if(!didComplete){
                completeWorkout.visibility = View.VISIBLE
                writeDiary.visibility = View.GONE
            }else if(didComplete && diaryData==null){
                completeWorkout.visibility = View.GONE
                writeDiary.visibility = View.VISIBLE
            }else if (didComplete && diaryData!=null){
                completeWorkout.visibility = View.GONE
                writeDiary.visibility = View.GONE
            }

        }
        // 루틴 데이터 - 이날 요일에 해당하는 데이터 리스트 리턴 받기
        Toast.makeText(activity,"현재", Toast.LENGTH_SHORT).show()

    }

    private fun initOwnList(){
        // 클릭 시 마다 배열 새로
        ownList.add(OwnListData(GregorianCalendar(2022,6,5),false, "팔굽혀펴기","어깨", 5,1))
        ownList.add(OwnListData(GregorianCalendar(2022,6,5),true, "플랭크","코어", 3,3))
        ownList.add(OwnListData(GregorianCalendar(2022,6,7),true, "윗몸일으키기","코어", 3,3))
        ownList.add(OwnListData(GregorianCalendar(2022,6,7),true, "달리기","다리", 1,2))

        // 리사이클러 뷰 어댑터
        ownListAdapter = OwnListAdapter(ownList,today)
        ownListAdapter.onItemClickListener= object : OwnListAdapter.OnItemClickListener{
            override fun onClick(p0: View?) {
                val fragTransaction = requireActivity().supportFragmentManager.beginTransaction()
//                fragTransaction.replace(R.id.container,) // workout fragment으로 전환
                fragTransaction.commit()
            }

        }
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