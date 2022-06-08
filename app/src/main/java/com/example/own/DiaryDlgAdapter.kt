package com.example.own

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.own.databinding.DiaryDlgRowBinding

class DiaryDlgAdapter(val workouts:ArrayList<OwnListData>):RecyclerView.Adapter<DiaryDlgAdapter.ViewHolder>(){
    //val workouts: ArrayList<> <- 하지라님 클래스로 변경해야함
    inner class ViewHolder(val binding: DiaryDlgRowBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = DiaryDlgRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
//            diaryDlgEmoji
//            diaryDlgWorkoutName
//            diaryDlgWorkoutSub
//            diaryDlgOneLine
            // 초기화 해주기

        }
    }

    override fun getItemCount(): Int {
        return workouts.size
    }


}