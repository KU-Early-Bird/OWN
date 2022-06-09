package com.example.own.Routine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.own.MainActivity
import com.example.own.OwnDBHelper
import com.example.own.R
import com.example.own.databinding.FragmentRoutineBinding
import com.example.own.databinding.FragmentRoutineWriteBinding

class RoutineWriteFragment : Fragment() {
    var rtData = RoutineData()
    var binding: FragmentRoutineWriteBinding?=null
    lateinit var rtDBHelper:OwnDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rtDBHelper = (activity as MainActivity).dbhelper
        setFragmentResultListener("rtdata"){requestKey, bundle ->
            rtData.id = bundle.getInt("id")
            rtData = rtDBHelper.findRoutine(rtData.id)
            Log.d("routine",rtData.toString())

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutineWriteBinding.inflate(inflater,container,false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            complete.setOnClickListener {
                rtData.name = nameedit.text.toString()
                rtData.bodyPart = bodyPartedit.text.toString()
                rtData.setNum = setNumedit.text.toString().toInt()
                rtData.time = Timesedit.text.toString().toInt()
                rtData.restTime = restTimeedit.text.toString().toInt()
                rtData.partTime = partTimedit.text.toString().toInt()
                rtData.type = typecheckbox.isChecked
                rtData.sound = soundcheckbox.isChecked
                rtData.isDay = isdaycheckbox.isChecked
                rtData.dayOfWeek[0] = suncheckbox.isChecked
                rtData.dayOfWeek[1] = moncheckbox.isChecked
                rtData.dayOfWeek[2] = tuecheckbox.isChecked
                rtData.dayOfWeek[3] = wedcheckbox.isChecked
                rtData.dayOfWeek[4] = thucheckbox.isChecked
                rtData.dayOfWeek[5] = fricheckbox.isChecked
                rtData.dayOfWeek[6] = satcheckbox.isChecked
                if (rtDBHelper.findRoutine(rtData.id) != null)
                    rtDBHelper.insertRoutine(rtData)
                else
                    rtDBHelper.updateRoutine(rtData)
                parentFragmentManager.beginTransaction().replace(R.id.container, RoutineFragment()).commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}