package com.example.own

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.own.databinding.DailyWorkoutRowBinding

class OwnListAdapter(val items:ArrayList<OwnListData>) : RecyclerView.Adapter<OwnListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: DailyWorkoutRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = DailyWorkoutRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.binding.apply {
            // 아이템 리스트(<- 클릭 날짜마다 달라짐) 의 값들로 채워 넣기
            // emoji
            when(items[pos]){
//                0->
//                1->
//                2->
            }
            ownListEmoji.setImageResource(R.drawable.ic_baseline_mood_24)
            ownListWorkoutName.text = items[pos].name
            ownListBodyPart.text = items[pos].bodyPart
            ownListSet.text = items[pos].set.toString()+"세트"

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}