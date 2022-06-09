package com.example.own.Routine

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.own.MainActivity
import com.example.own.OwnDBHelper
import com.example.own.R
import com.example.own.databinding.FragmentRoutineBinding

class RoutineFragment : Fragment(){
    lateinit var binding: FragmentRoutineBinding
    lateinit var rtDBHelper: OwnDBHelper
    lateinit var rtRecyclerView: RecyclerView
    lateinit var rtAdapter: RoutineAdapter
    var rtData:ArrayList<RoutineData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutineBinding.inflate(inflater,container,false)
        initDB()
        initRecyclerView()
        binding.addroutine.setOnClickListener{
            childFragmentManager.beginTransaction().replace(R.id.container,RoutineWriteFragment()).commit()
        }
        return binding.root
    }

    private fun initDB(){
        rtDBHelper = (activity as MainActivity).dbhelper
        rtData = rtDBHelper.getAllRoutine()
    }

    private fun initRecyclerView(){
        rtRecyclerView = binding.routinerecyclerview
        rtRecyclerView.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.VERTICAL, false)
        rtAdapter = RoutineAdapter(rtData)
        rtAdapter.itemClickListener = object : RoutineAdapter.OnItemClickListener{
            override fun OnItemClick(data:RoutineData){
                var rtdata = Bundle()
                rtdata.putInt("id",data.id)
                setFragmentResult("rtdata", rtdata)
                parentFragmentManager.beginTransaction().replace(R.id.container,RoutineWriteFragment()).commit()
            }
        }
        rtRecyclerView.adapter = rtAdapter
    }

}