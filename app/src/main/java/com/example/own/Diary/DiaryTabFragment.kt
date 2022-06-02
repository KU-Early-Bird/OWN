package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.own.databinding.FragmentDiaryTabBinding
import com.example.own.R

class DiaryTabFragmentFragment : Fragment() {
    private lateinit var binding:FragmentDiaryTabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiaryTabBinding.inflate(inflater,container,false)

        binding.diaryread.setOnClickListener{
            val bundle = bundleOf("key" to "date")
            setFragmentResult("requestKey", bundle)
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.DiaryGallery, DiaryReadFragment())
                addToBackStack(null)
                commit()
            }
        }
        return binding.root
    }

}