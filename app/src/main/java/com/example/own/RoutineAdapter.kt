package com.example.routinetab

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RoutineAdapter(val items:ArrayList<RoutineData>) : RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:RoutineData)
    }

    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)
        val bodyPart = itemView.findViewById<TextView>(R.id.bodyPart)
        val date = itemView.findViewById<TextView>(R.id.date)
        val sets = itemView.findViewById<TextView>(R.id.sets)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.routine_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = items[position].name
        holder.bodyPart.text = items[position].bodyPart

        if(items[position].isDay)
            holder.date.text = "오늘"
        else {
            var str = ""
            if(items[position].dayOfWeek[0])
                str += "일"
            if(items[position].dayOfWeek[1])
                str += "월"
            if(items[position].dayOfWeek[2])
                str += "화"
            if(items[position].dayOfWeek[3])
                str += "수"
            if(items[position].dayOfWeek[4])
                str += "목"
            if(items[position].dayOfWeek[5])
                str += "금"
            if(items[position].dayOfWeek[6])
                str += "토"
            holder.date.text = str
        }
        holder.sets.text = items[position].setNum.toString() + "회"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}