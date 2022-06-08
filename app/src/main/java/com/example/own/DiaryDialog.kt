package com.example.own

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.own.Diary.DiaryData
import com.example.own.databinding.DiaryDialogBinding
import java.io.File


class DiaryDialog(var diaryData: DiaryData) : DialogFragment() {
    lateinit var binding:DiaryDialogBinding
    lateinit var adapter: DiaryDlgAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DiaryDlgAdapter(ArrayList<OwnListData>()) // 하지라님 데이터로 바꾸기

        binding.apply {
            // 이미지 소스로 이미지 보여주기
            var imagePath=diaryData.Diary_Image
            if (File(imagePath).exists()) {
                var bitmap = BitmapFactory.decodeFile(imagePath)
                binding.diaryDlgImg.setImageBitmap(bitmap)
            }

            // 일기 내용
            diaryDlgtext.text = diaryData.Diary_Content

            // recycler view
            diaryDlgRecycler.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            diaryDlgRecycler.adapter = adapter

            //
            diaryDlgDismissBtn.setOnClickListener {
                dismiss()
            }

        }


    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
    }


}