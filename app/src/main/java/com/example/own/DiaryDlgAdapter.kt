package com.example.own

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.own.Workout.WorkoutData
import com.example.own.databinding.DiaryDlgRowBinding

class DiaryDlgAdapter(val workouts:ArrayList<WorkoutData>):RecyclerView.Adapter<DiaryDlgAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: DiaryDlgRowBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = DiaryDlgRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.binding.apply {
            when(workouts[pos].emojiID){
                1-> {
                    diaryDlgEmoji.setImageResource(R.drawable.icon_emoji1_background)
                    diaryDlgEmoji.setColorFilter(Color.GREEN)
                }
                2-> {
                    diaryDlgEmoji.setImageResource(R.drawable.icon_emoji2_background)
                    diaryDlgEmoji.setColorFilter(Color.YELLOW)
                }
                3-> {
                    diaryDlgEmoji.setImageResource(R.drawable.icon_emoji3_background)
                    diaryDlgEmoji.setColorFilter(Color.RED)
                }
            }

            diaryDlgWorkoutName.text = workouts[pos].workoutName
            diaryDlgWorkoutSub.text = workouts[pos].bodyPart.toString() +
                    ", "+  workouts[pos].set.toString()+"세트"
            diaryDlgOneLine.text = workouts[pos].assessment

        }
    }

    override fun getItemCount(): Int {
        return workouts.size
    }


}