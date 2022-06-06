package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.DiaryAdapter
import com.example.myapplication.DiaryData
import com.example.myapplication.MyDBManager
import com.example.myapplication.MainActivity
import com.example.myapplication.databinding.FragmentDiaryTabBinding

class DiaryTabFragment : Fragment() {
    lateinit var binding:FragmentDiaryTabBinding
    lateinit var diaryadapter: DiaryAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var DList:ArrayList<DiaryData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //set binding
        binding = FragmentDiaryTabBinding.inflate(inflater,container,false)

        /*  database declared on MainActivity,
        *   for using dbhelper, must use (activity as MainActivity)
        *
        */
        var dbhelper = (activity as MainActivity).dbhelper
        DList= dbhelper.getAllDiary()

        // give adapter the ArrayList of items that would be inserted inside recyclerview
        diaryadapter =  DiaryAdapter(DList)

        //declare OnClickListener of each item row
        diaryadapter.itemClickListener = object : DiaryAdapter.OnItemClickListener{
            override fun onItemClick(data: DiaryData) {
                //bundled data will be sent across fragment
                var bundle = Bundle()
                bundle.putString("date", data.Diary_Date)
                bundle.putString("content", data.Diary_Content)
                bundle.putString("image", data.Diary_Image)
                setFragmentResult("DiaryRead", bundle)

                //open fragment above R.id.* (CurrentTab)
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.DiaryTab, DiaryReadFragment())
                    addToBackStack(null)
                    commit()

                }
            }
        }

        //give recyclerview to adapter
        binding.diaryRecyclerview.adapter=diaryadapter
        //linear/grid/etc layoutmanager setting
        layoutManager = GridLayoutManager(activity,3)
        //give layoutmanager to adapter
        binding.diaryRecyclerview.layoutManager=layoutManager
        return binding.root
    }

    override fun onResume() {
        //if new diary had added to diaryList, refresh the recyclerview
        super.onResume()
        diaryadapter.notifyDataSetChanged()
    }
}