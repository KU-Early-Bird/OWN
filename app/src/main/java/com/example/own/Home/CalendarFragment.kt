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
import com.example.own.DB.OwnDBHelper
import com.example.own.Diary.DiaryData
import com.example.own.Diary.DiaryDialog
import com.example.own.Diary.DiaryWriteFragment
import com.example.own.Home.*
import com.example.own.Workout.WorkoutFragment
import com.example.own.databinding.FragmentCalenderBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {

    lateinit var achieveTableData: AchieveTableData
    // calendar
    var today:GregorianCalendar?=null
    var clickedDate:GregorianCalendar?=null
    val dateFomatter = SimpleDateFormat("yyyy-MM-dd")
    lateinit var mainActivity: MainActivity

    // ownList members
    lateinit var ownListAdapter: OwnListAdapter
    var ownList=ArrayList<OwnListData>()
    var diaryData:DiaryData?=null

    // achieve members
    var binding:FragmentCalenderBinding ?=null
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
        achieveTableData = ownDBHelper.readAchieve()

        // 루틴 데이터 workout 데이터에 저장
        // 앱 켤 때마다 workout 데이터 읽어서 오늘 조건에 맞는거 보여주기

        // 오늘 날짜 ownList 가져오기
        ownList=ownDBHelper.getWorkoutOwnList(getTodayGregorian())

        // 오늘 기록 했는지 저장
        diaryData =ownDBHelper.getDiaryData(CalendarDay.today())
//        Log.d("didRecord",didRecord.toString())

    }


    private fun initLayer() {

        // ownList adapter 초기화
        initOwnList()

        // Calender 초기화 - 항상 initOwnList 보다 뒤에 와야 함
        initCalender()

        // btn 초기화
        initButton()

        // 오늘 날짜에 맞춰 화면 구성
        initTodayLayout()
        Log.d("didWorkout",(diaryData==null).toString())

    }

    private fun initOwnList(){
        // 클릭 시 마다 배열 새로
//        ownList.add(OwnListData(GregorianCalendar(2022,6,5),false, "팔굽혀펴기","어깨", 5,1))
//        ownList.add(OwnListData(GregorianCalendar(2022,6,5),true, "플랭크","코어", 3,3))
//        ownList.add(OwnListData(GregorianCalendar(2022,6,7),true, "윗몸일으키기","코어", 3,3))
//        ownList.add(OwnListData(GregorianCalendar(2022,6,7),true, "달리기","다리", 1,2))

        // 리사이클러 뷰 어댑터
        ownListAdapter = OwnListAdapter(ownList,today)
        ownListAdapter.onItemClickListener= object : OwnListAdapter.OnItemClickListener{
            override fun onClick(p0: View?) {
                val fragTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragTransaction.replace(R.id.container,WorkoutFragment()).commit() // workout fragment으로 전환
            }
        }

        binding!!.apply {
            ownListRecyclerView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
            ownListRecyclerView.adapter = ownListAdapter
        }
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
        binding!!.calendarView.setOnDateChangedListener { _, date, _ ->
            if(today == null)
                today = getTodayGregorian()

            clickedDate = GregorianCalendar(date.year,date.month-1,date.day)
            diaryData = ownDBHelper.getDiaryData(clickedDate!!); // 기록 데이터 존재여부 확인

            // 운동 데이터 - 날짜에 해당하는 데이터 불러오기
            ownList = ownDBHelper.getWorkoutOwnList(clickedDate!!)
            ownListAdapter.items = ownList
            ownListAdapter.notifyDataSetChanged()

            // 과거
            if(clickedDate!!.compareTo(today)<0){
                // 운동 완료 버튼 안보이게 처리 (자동 완료 처리되기 때문)
                binding!!.completeWorkout.visibility = View.GONE

                if(diaryData!=null){
                    binding!!.writeDiary.visibility = View.GONE
                }else{
                    binding!!.writeDiary.visibility = View.VISIBLE
                }


            }
            // 미래
            else if(clickedDate!!.compareTo(today)>0){
                ownList = ownDBHelper.getRoutineOwnList(clickedDate!!)
                // 운동 완료 & 기록 작성 버튼 안보이도록
                binding!!.apply {
                    writeDiary.visibility = View.GONE
                    completeWorkout.visibility =View.GONE
                }

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

    private fun initButton() {
        initTodayLayout()

        // 오운완 버튼
        binding!!.apply {
            completeWorkout.setOnClickListener {
                achieveTableData.ownwanDays+=1
                ownDBHelper.updateAchieve(achieveTableData.ownwanDays, true)
                achieveTableData = ownDBHelper.readAchieve()
                initTodayLayout()
                activity?.recreate()
            }

            writeDiary.setOnClickListener{
                //bundled data will be sent across fragment
                val date = dateFomatter.format(clickedDate!!.time)
                var bundle = Bundle()
                bundle.putString("date", date)
                setFragmentResult("DiaryWrite", bundle)
                writeDiary.visibility=View.GONE

                parentFragmentManager.beginTransaction().apply{
                    replace(R.id.container, DiaryWriteFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    public fun getTodayGregorian():GregorianCalendar{
        val today = Calendar.getInstance()
        return GregorianCalendar(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH) )
    }



    private fun initTodayLayout(){
//        // 운동 완료 여부와 기록 여부에 따라  - 운동 DB & 기록 DB 에서 받아오기(?)

        binding!!.apply {
            if(!achieveTableData.didWorkout){
                completeWorkout.visibility = View.VISIBLE
                writeDiary.visibility = View.GONE
            }else if(achieveTableData.didWorkout && diaryData==null){
                completeWorkout.visibility = View.GONE
                writeDiary.visibility = View.VISIBLE
            }else if (achieveTableData.didWorkout && diaryData!=null){
                completeWorkout.visibility = View.GONE
                writeDiary.visibility = View.GONE
            }

        }
    }




    override fun onDestroy() {
        super.onDestroy()
    }
}