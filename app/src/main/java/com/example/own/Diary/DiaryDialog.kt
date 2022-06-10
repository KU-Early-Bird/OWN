package com.example.own.Diary

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.own.DB.OwnDBHelper
import com.example.own.Others.Converter
import com.example.own.databinding.DiaryDialogBinding
import java.io.File


class DiaryDialog(var diaryData: DiaryData) : DialogFragment() {
    lateinit var binding:DiaryDialogBinding
    lateinit var adapter: DiaryDlgAdapter
    lateinit var ownDBHelper: OwnDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable=false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DiaryDialogBinding.inflate(inflater,container,false)
        return binding.root!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ownDBHelper = OwnDBHelper(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateStr = Converter().convertStrToCalender(diaryData.Diary_Date)
        val workoutList = ownDBHelper.getWorkoutList(dateStr)
        adapter = DiaryDlgAdapter(workoutList)


        // 이미지 소스로 이미지 보여주기
        var imagePath=diaryData.Diary_Image
        if (File(imagePath).exists()) {
            var bitmap = BitmapFactory.decodeFile(imagePath)
            binding.diaryDlgImg.setImageBitmap(bitmap)
        }

        // 일기 내용
        binding.diaryDlgtext.text = diaryData.Diary_Content

        // recycler view
        binding.diaryDlgRecycler.layoutManager = LinearLayoutManager(activity ,LinearLayoutManager.VERTICAL,false)
        binding.diaryDlgRecycler.adapter = adapter

        //
        binding.diaryDlgDismissBtn.setOnClickListener {
            dismiss()
        }


    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }


}