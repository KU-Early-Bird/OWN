package com.example.own.Workout

import com.example.own.databinding.WorkoutRowBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WorkoutAdapter (val items:ArrayList<WorkoutData>, val today: String?): RecyclerView.Adapter<WorkoutAdapter.ViewHolder>(){

    var nowCount: Int = 0
    var nowSet: Int = 0
    var isItemClick: Boolean = false

    interface OnItemClickListener:View.OnClickListener{
        fun onItemClickListener(workoutData: WorkoutData){
        }
    }

    var onItemClickListener: OnItemClickListener ?= null

    inner class ViewHolder(val binding: WorkoutRowBinding): RecyclerView.ViewHolder(binding.root){


    }

    fun moveItem(oldPos: Int, newPos:Int){
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos:Int){
//        items.removeAt(pos)
//        notifyItemRemoved(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WorkoutRowBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.workoutName.text = items[position].workoutName
        holder.binding.countText.text = "COUNT: " + nowCount.toString() + items[position].count.toString()
        holder.binding.workoutSetText.text = "SET: " + nowSet.toString() + items[position].set.toString()

//        holder.binding.apply {
//            if(items[position].isDone) removeItem(position)
//        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}