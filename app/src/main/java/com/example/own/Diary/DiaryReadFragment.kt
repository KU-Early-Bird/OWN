package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.example.own.databinding.FragmentDiaryReadBinding
import java.util.*


class DiaryReadFragment : Fragment() {
    var diarydate = "temporary"
    var imagepath = "data/data/com.example.myapplication/files/diaryimage0.jpg"
    lateinit var binding:FragmentDiaryReadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //날짜 값 받아오기
        setFragmentResultListener("requestKey") { key, bundle ->
            diarydate = bundle.getString("key").toString()
            binding.DiaryContent.setText(diarydate)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryReadBinding.inflate(inflater, container, false)

        getimagediary()

        return binding.root
    }

    private fun getimagediary() {
        var bitmap = BitmapFactory.decodeFile(imagepath)
        binding.imageread.setImageBitmap(bitmap)
    }


    companion object {

    }
}