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
    var rtbundle = Bundle()
    lateinit var rtDBHelper:OwnDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rtDBHelper = (activity as MainActivity).dbhelper
        setFragmentResultListener("rtdata"){requestKey, bundle ->
            rtbundle = bundle
            rtData.id = bundle.getInt("id")
            var clicked = bundle.getInt("clicked")
            rtData = rtDBHelper.findRoutine(rtData.id)

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
            if(rtbundle.getInt("clicked") == 1) {
                nameedit.setText(rtData.name)
                bodyPartedit.setText(rtData.bodyPart)
                setNumedit.setText(rtData.setNum.toString())
                Timesedit.setText(rtData.time.toString())
                restTimeedit.setText(rtData.restTime.toString())
                partTimedit.setText(rtData.partTime.toString())
                typecheckbox.setOnCheckedChangeListener { _, isChecked ->
                    typecheckbox.isChecked = rtData.type
                }
                soundcheckbox.setOnCheckedChangeListener { _, isChecked ->
                    soundcheckbox.isChecked = rtData.sound
                }
                isdaycheckbox.setOnCheckedChangeListener { _, isChecked ->
                    isdaycheckbox.isChecked = rtData.isDay
                }
                suncheckbox.setOnCheckedChangeListener { _, isChecked ->
                    suncheckbox.isChecked = rtData.dayOfWeek[0]
                }
                moncheckbox.setOnCheckedChangeListener { _, isChecked ->
                    moncheckbox.isChecked = rtData.dayOfWeek[1]
                }
                tuecheckbox.setOnCheckedChangeListener { _, isChecked ->
                    tuecheckbox.isChecked = rtData.dayOfWeek[2]
                }
                wedcheckbox.setOnCheckedChangeListener { _, isChecked ->
                    wedcheckbox.isChecked = rtData.dayOfWeek[3]
                }
                thucheckbox.setOnCheckedChangeListener { _, isChecked ->
                    thucheckbox.isChecked = rtData.dayOfWeek[4]
                }
                fricheckbox.setOnCheckedChangeListener { _, isChecked ->
                    fricheckbox.isChecked = rtData.dayOfWeek[5]
                }
                satcheckbox.setOnCheckedChangeListener { _, isChecked ->
                    satcheckbox.isChecked = rtData.dayOfWeek[6]
                }
            }
            //typecheckbox.setOnCheckedChangeListener{}
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
