package com.example.own.Workout

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.own.DB.OwnDBHelper
import com.example.own.R
import com.example.own.databinding.FragmentWorkoutBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

class WorkoutFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: WorkoutAdapter
    var _binding: FragmentWorkoutBinding? = null
    var counter: Int = 0
    private var isStopwatch: Boolean = true
    private var emojiID: Int = 0
    private var userAssessment: String = ""
    private lateinit var dlgView: View
    private lateinit var dlgWindow: AlertDialog
    private lateinit var timer: CountDownTimer
    private lateinit var timerStart: Button
    private lateinit var timerStop: Button
    private lateinit var timerPause: Button
    private lateinit var timerResume: Button
    lateinit var countTime: TextView
    var milileft: Long = 0
    var flagTimer: Boolean = true
    private var workoutList = ArrayList<WorkoutData>()
    private lateinit var today: Date
    private var todayDateInString: String = ""
    lateinit var WorkoutDBHelper: OwnDBHelper
    var dateToday: GregorianCalendar?=null
    var workoutID=0
    var progressBarCount: Int = 0
    var progressBarSet: Int = 0
    lateinit var test:TextView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val root: View = binding.root
        init()
        initWorkoutData()
        initRecyclerView()
        return root
    }
    public fun getTodayGregorian():GregorianCalendar{
        val today = Calendar.getInstance()
        return GregorianCalendar(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH) )
    }

    private fun initWorkoutData() {
//        dateToday = getTodayGregorian()
//         workoutList = WorkoutDBHelper.getRoutineWorkoutList(dateToday!!)!!
//            for (item in workoutList) println(item )
////    }
        workoutList.add(
            WorkoutData(
                1,
                todayDateInString,
                "Plank",
                "abdominal",
                "",
                2,
                5,
                "",
                15,
                5,
                0,
                false,
                1,
                false,
                0,
                "",
                0,
                ""

            )
        )
        workoutList.add(
            WorkoutData(
                2,
                todayDateInString,
                "Deadlifts",
                "legs",
                "",
                1,
                3,
                "",
                0,
                0,
                0,
                false,
                0,
                false,
                0,
                "",
                0,
                ""
            )
        )
        workoutList.add(
            WorkoutData(
                3,
                todayDateInString,
                "Calve raises",
                "legs",
                "",
                1,
                1,
                "",
                0,
                0,
                0,
                true,
                0,
                false,
                0,
                "",
                0,
                ""
            )
        )
        workoutList.add(
            WorkoutData(
                4,
                todayDateInString,
                "Chest dips",
                "chest",
                "",
                1,
                2,
                "",
                0,
                0,
                0,
                true,
                0,
                false,
                0,
                "",
                0,
                ""
            )
        )
    }

    private fun initTimer() {
        flagTimer = true
        timer = object : CountDownTimer(0, 0) {
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
            }
        }

        timerStart = binding.startBtn
        timerStop = binding.stopBtn
        timerPause = binding.pauseBtn
        timerResume = binding.resumeBtn
        countTime = binding.timerDisplay

        //counter = getWorkoutTimerChallenge()

        timerStart.setOnClickListener {
            if(workoutList[workoutID].type == 0) startStopwatch(6000000)
            else startTimer(workoutList[workoutID].partTime.toLong())
        }

        timerStop.setOnClickListener {
            if(workoutList[workoutID].isDone)
//                Toast.makeText(binding.root.context, "운동을 완료하지 않습니다", Toast.LENGTH_LONG).show()
//            else{
                showDialog()
//            }
            stopTimer()
        }
        timerPause.setOnClickListener { pauseTimer() }
        timerResume.setOnClickListener { resumeTimer() }
    }

    private fun startTimer(milli: Long) {
        binding.timerState.text = "TIMER"
        if (milli != milileft)
            counter = workoutList[workoutID].partTime
        countTime.text = String.format(
            "%02d:%02d:%02d", counter / 3600,
            (counter % 3600) / 60, (counter % 60))
        flagTimer = true

//        if(timer == null) {
            timer = object : CountDownTimer(milli, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    milileft = millisUntilFinished
                    countTime.text = String.format(
                        "%02d:%02d:%02d", counter / 3600,
                        (counter % 3600) / 60, (counter % 60)
                    )
                    counter--
                }

                override fun onFinish() {
                    flagTimer = false
                    countTime.text = "00:00:00"
                    playAudio()
                    nowCount++
                    if (nowCount == workoutList[workoutID].count) {
                        nowCount = 0
                        nowSet++
                    }

                    if (nowSet == workoutList[workoutID].set) {
                        nowSet = 0
                        showDialog()
                        Toast.makeText(binding.root.context, "TIME IS UP!", Toast.LENGTH_LONG)
                            .show()
                    }
//                else if (nowSet!=workoutList[workoutID].set)
                startRestTimer(workoutList[workoutID].restTime.toLong())
                }
            }.start()
//        }
    }

    private fun startRestTimer(milli: Long) {
        playAudio()
        if (milli != milileft) counter = workoutList[workoutID].restTime
        binding.timerState.text = "REST TIME"
        countTime.text = String.format(
            "%02d:%02d:%02d", counter / 3600,
            (counter % 3600) / 60, (counter % 60))
        flagTimer = true

        if(timer == null) {
            timer = object : CountDownTimer(milli, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    milileft = millisUntilFinished
                    countTime.text = String.format(
                        "%02d:%02d:%02d", counter / 3600,
                        (counter % 3600) / 60, (counter % 60)
                    )
                    counter--
                }

                override fun onFinish() {
                    flagTimer = false
                    countTime.text = "00:00:00"
                    playAudio()
                    nowCount++
                    if (nowCount == workoutList[workoutID].count) {
                        nowCount = 0
                        nowSet++
                    }

                    if (nowSet == workoutList[workoutID].set) {
                        nowSet = 0
                        showDialog()
                        Toast.makeText(binding.root.context, "TIME IS UP!", Toast.LENGTH_LONG)
                            .show()
                        duration =
                            (workoutList[workoutID].partTime * workoutList[workoutID].count) * workoutList[workoutID].set
                    }
                    progressBarCount =
                        progressBarCount + (nowCount / workoutList[workoutID].count) * 100
                    workoutList[workoutID].progressBarCountText =
                        nowCount.toString() + "/" + workoutList[workoutID].count
                    workoutList[workoutID].progressBarCount = progressBarCount

                    progressBarSet = progressBarSet + (nowSet / workoutList[workoutID].set) * 100
                    workoutList[workoutID].progressBarSetText =
                        nowSet.toString() + "/" + workoutList[workoutID].set
                    workoutList[workoutID].progressBarSet = progressBarSet
                    adapter.notifyItemChanged(workoutID)

                    startTimer(workoutList[workoutID].partTime.toLong())
                }
            }.start()
        }
    }


    private fun startStopwatch(milli: Long) {
        if (milli != milileft) counter = workoutList[workoutID].restTime
        binding.timerState.text = "STOPWATCH"
        countTime.text = "00:00:00"
        counter = 0
        flagTimer = true

        if(timer == null) {
            timer = object : CountDownTimer(milli, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    milileft = millisUntilFinished
                    countTime.text = String.format(
                        "%02d:%02d:%02d", counter / 3600,
                        (counter % 3600) / 60, (counter % 60)
                    )
                    if (counter != 0 && counter % 30 == 0) playAudio()
                    counter++
                }

                override fun onFinish() {
                    flagTimer = false
                    countTime.text = "00:00:00"
                    playAudio()
                }
            }.start()
        }
        duration = counter
    }

    private fun pauseTimer() {
        timer.cancel()
        flagTimer = true
    }

    private fun resumeTimer() {
        if (flagTimer && isStopwatch) startStopwatch(milileft)
        else if (flagTimer && !isStopwatch) startTimer(milileft)
    }

    var nowCount: Int = 0
    var nowSet: Int = 0
    var duration:Int = 0

    private fun stopTimer() {
        if(workoutList[workoutID].type == 0){
            nowCount++
            duration += counter
            workoutList[workoutID].count

            if(nowCount == workoutList[workoutID].count){
                nowCount = 0
                nowSet++
            }

            if(nowSet == workoutList[workoutID].set){
                nowSet = 0
                showDialog()
            }
            else Toast.makeText(binding.root.context, "운동을 완료하지 않습니다", Toast.LENGTH_LONG).show()
            progressBarCount = progressBarCount + (nowCount / workoutList[workoutID].count) * 100
            workoutList[workoutID].progressBarCountText = nowCount.toString() + "/" + workoutList[workoutID].count
            workoutList[workoutID].progressBarCount = progressBarCount

            progressBarSet = progressBarSet + (nowSet / workoutList[workoutID].set) * 100
            workoutList[workoutID].progressBarSetText = nowSet.toString() + "/" + workoutList[workoutID].set
            workoutList[workoutID].progressBarSet = progressBarSet
            adapter.notifyItemChanged(0)
        }else {
//            duration =( workoutList[workoutID].partTime * workoutList[workoutID].count ) * workoutList[workoutID].set
            Toast.makeText(binding.root.context, "운동을 완료하지 않습니다", Toast.LENGTH_LONG).show()
        }

        timer.cancel()
        timer.onFinish()
        workoutList[workoutID].duration = String.format(
            "%02d:%02d:%02d", duration / 3600,
            (duration % 3600) / 60, (duration % 60)
        )
    }

    private fun playAudio() {
        val BgMusic = MediaPlayer.create(binding.root.context, R.raw.noti)
        BgMusic?.start()
    }

    private fun initRecyclerView() {

        recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.VERTICAL, false
            )

            adapter = WorkoutAdapter(workoutList, todayDateInString)
            recyclerView.adapter = adapter
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        adapter.ItemClickListener = object : WorkoutAdapter.OnItemClickListener {
            override fun onItemClick(workoutData: WorkoutData, position: Int) {
                workoutData.isClicked = !workoutData.isClicked
                adapter.notifyItemChanged(position)
                workoutID = position
                initTimer()
            }
            override fun onClick(v: View?) {
            }
        }
    }

    private fun init() {
    timerStart = binding.startBtn
    timerStart.setOnClickListener {
        Toast.makeText(binding.root.context, "운동을 하나 선택하세요", Toast.LENGTH_LONG).show()
    }

//        ===================== initialization of dialog =========================
        dlgView = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.custom_dialog, container, false)
        dlgWindow = AlertDialog.Builder(binding.root.context)
            .setView(dlgView)
            .setCancelable(false)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }.create()

        val completeWorkoutBtn = binding.completeWorkoutBtn
        completeWorkoutBtn.setOnClickListener {
            Toast.makeText(binding.root.context, "일기탭에 이동", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showDialog() {
        dlgWindow.setOnShowListener {
            val btn = dlgWindow.getButton(AlertDialog.BUTTON_POSITIVE)
            btn.setTextColor(getColor(binding.root.context, R.color.customDarkBlue))
            btn.gravity = Gravity.CENTER
            btn.textSize = 16F
            btn.setBackgroundColor(getColor(binding.root.context, R.color.customBlue))

            btn.setOnClickListener {
                if (emojiID == 0) {
                    Toast.makeText(binding.root.context, "이모지를 선택하세요", Toast.LENGTH_LONG).show()
                } else {
                    userAssessment = dlgView.comment.text.toString()
                    dlgView.comment.text?.clear()
                    dlgWindow.dismiss()
                    workoutList[workoutID].emojiID = emojiID
                    workoutList[workoutID].assessment = userAssessment
                }
            }
        }

//      Set custom title for dialog
        val title = TextView(binding.root.context)
        title.text = "운동은 어떠셨나요?"
        title.setBackgroundColor(getColor(binding.root.context, R.color.customBlue))
        title.setPadding(0, 30, 0, 30)
        title.gravity = Gravity.CENTER
        title.setTextColor(getColor(binding.root.context, R.color.customDarkBlue))
        title.textSize = 20F

        val radioGroup = dlgView.findViewById<RadioGroup>(R.id.radioGroup)
        val btn1 = dlgView.findViewById<RadioButton>(R.id.emoji1btn)
        val btn2 = dlgView.findViewById<RadioButton>(R.id.emoji2btn)
        val btn3 = dlgView.findViewById<RadioButton>(R.id.emoji3btn)

        dlgWindow.setCustomTitle(title)
        dlgWindow.show()
        dlgWindow.withCenteredButtons()

        btn1.background.setTint(Color.GRAY)
        btn2.background.setTint(Color.GRAY)
        btn3.background.setTint(Color.GRAY)

        emojiID = 0
        userAssessment = ""

        radioGroup.setOnCheckedChangeListener { radioGroup, checkedID ->
            if (checkedID == R.id.emoji1btn) {
                emojiID = 1
                btn1.background.setTint(Color.GREEN)
                btn2.background.setTint(Color.GRAY)
                btn3.background.setTint(Color.GRAY)
            } else if (checkedID == R.id.emoji2btn) {
                emojiID = 2
                btn2.background.setTint(Color.YELLOW)
                btn1.background.setTint(Color.GRAY)
                btn3.background.setTint(Color.GRAY)
            } else if (checkedID == R.id.emoji3btn) {
                emojiID = 3
                btn3.background.setTint(Color.RED)
                btn1.background.setTint(Color.GRAY)
                btn2.background.setTint(Color.GRAY)
            }
        }
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timer?.cancel()
    }

    private fun AlertDialog.withCenteredButtons() {
        val positive = getButton(AlertDialog.BUTTON_POSITIVE)
        val negative = getButton(AlertDialog.BUTTON_NEGATIVE)

        //Disable the material spacer view in case there is one
        val parent = positive.parent as? LinearLayout
        parent?.gravity = Gravity.CENTER_HORIZONTAL
        val leftSpacer = parent?.getChildAt(1)
        leftSpacer?.visibility = View.GONE

        //Force the default buttons to center
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.weight = 0.1f
        layoutParams.gravity = Gravity.CENTER

        positive.layoutParams = layoutParams
        negative.layoutParams = layoutParams
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }
}