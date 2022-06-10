package com.example.own.Workout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.own.R
import com.example.own.databinding.FragmentWorkoutBinding
import com.example.own.databinding.WorkoutRowBinding
import kotlinx.android.synthetic.main.workout_row.view.*

class WorkoutAdapter (val items:ArrayList<WorkoutData>, val today: String?): RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    interface OnItemClickListener:View.OnClickListener{
        fun onItemClick(workoutData: WorkoutData, position: Int){
        }
    }

    var ItemClickListener: OnItemClickListener ?= null

    inner class ViewHolder(val binding: WorkoutRowBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.workoutRow.setOnClickListener {
                ItemClickListener?.onItemClick(items[adapterPosition], adapterPosition )
            }
        }
    }

//    fun postAndNotifyAdapter(
//        handler: Handler,
//        recyclerView: RecyclerView,
//        adapter: RecyclerView.Adapter<*>
//    ) {
//        handler.post(Runnable {
//            if (!recyclerView.isComputingLayout) {
//                recyclerView.adapter = adapter
//                recyclerView.setOnClickListener {
//                    it.setBackgroundResource(R.drawable.rounded_corner_clicked)
////                    recyclerView.setBackgroundResource(R.drawable.rounded_corner_clicked)
//                }
//            } else {
//                postAndNotifyAdapter(handler, recyclerView, adapter)
//            }
//        })
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WorkoutRowBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(items[position].isClicked){
            holder.binding.workoutRow.setBackgroundResource(R.drawable.rounded_corner_clicked)
            holder.binding.workoutRow.alpha = 1.0F
        }
        else{
            holder.binding.workoutRow.setBackgroundResource(R.drawable.rounded_corner)
            holder.binding.workoutRow.alpha = 0.9F
        }

        holder.binding.workoutName.text = items[position].workoutName
        holder.binding.countText.text = "COUNT : 0/" + items[position].count.toString()
        holder.binding.workoutSetText.text = "SET : 0/" + items[position].set.toString()
        holder.binding.typeView.text = "TYPE: " + items[position].type + " | "
        holder.binding.workoutSetProgress.progress = items[position].progressBarSet
        holder.binding.countProgress.progress = items[position].progressBarCount

  }

    override fun getItemCount(): Int {
        return items.size
    }
}