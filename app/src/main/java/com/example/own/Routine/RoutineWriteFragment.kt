package com.example.own.Routine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.own.MainActivity
import com.example.own.OwnDBHelper
import com.example.own.databinding.FragmentRoutineBinding
import com.example.own.databinding.FragmentRoutineWriteBinding

class RoutineWriteFragment : Fragment() {
    lateinit var rtData:RoutineData
    lateinit var binding: FragmentRoutineWriteBinding
    var rtDBHelper = (activity as MainActivity).dbhelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("rtdata"){requestKey, bundle ->
            rtData.id = bundle.getInt("id")
            rtData = rtDBHelper.findRoutine(rtData.id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutineWriteBinding.inflate(inflater,container,false)
        binding.complete.setOnClickListener{
            rtData.name = binding.nameedit.text.toString()
            rtData.bodyPart = binding.bodyPartedit.text.toString()
            rtData.setNum = binding.setNumedit.text.toString().toInt()
            rtData.time = binding.Timesedit.text.toString().toInt()
            rtData.restTime = binding.restTimeedit.text.toString().toInt()
            rtData.partTime = binding.partTimedit.text.toString().toInt()
            rtData.type = binding.typecheckbox.isChecked
            rtData.sound = binding.soundcheckbox.isChecked
            rtData.isDay = binding.isdaycheckbox.isChecked
            rtData.dayOfWeek[0] = binding.suncheckbox.isChecked
            rtData.dayOfWeek[1] = binding.moncheckbox.isChecked
            rtData.dayOfWeek[2] = binding.tuecheckbox.isChecked
            rtData.dayOfWeek[3] = binding.wedcheckbox.isChecked
            rtData.dayOfWeek[4] = binding.thucheckbox.isChecked
            rtData.dayOfWeek[5] = binding.fricheckbox.isChecked
            rtData.dayOfWeek[6] = binding.satcheckbox.isChecked
            if(rtDBHelper.findRoutine(rtData.id) != null)
                rtDBHelper.insertRoutine(rtData)
            else
                rtDBHelper.updateRoutine(rtData)
        }
        return binding.root
    }
}