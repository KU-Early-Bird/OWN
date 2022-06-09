package com.example.own.Routine

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.example.own.databinding.RoutineRowBinding

class RoutineAdapter(val items:ArrayList<RoutineData>) : RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:RoutineData)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RoutineRowBinding) : RecyclerView.ViewHolder(binding.root){
        val text = binding.relativelayout
        init {
            text.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RoutineRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.id.text = items[position].id.toString()
        holder.binding.name.text = items[position].name
        holder.binding.bodyPart.text = items[position].bodyPart
        if(items[position].isDay)
                holder.binding.date.text = "오늘"
            else {
            var str = ""
            if (items[position].dayOfWeek[0])
                str += "일"
            if (items[position].dayOfWeek[1])
                str += "월"
            if (items[position].dayOfWeek[2])
                str += "화"
            if (items[position].dayOfWeek[3])
                str += "수"
            if (items[position].dayOfWeek[4])
                str += "목"
            if (items[position].dayOfWeek[5])
                str += "금"
            if (items[position].dayOfWeek[6])
                str += "토"
            holder.binding.date.text = str
        }
        holder.binding.sets.text = items[position].setNum.toString() + "회"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}