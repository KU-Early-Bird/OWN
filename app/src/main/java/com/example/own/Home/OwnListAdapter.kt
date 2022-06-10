package com.example.own.Home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.own.R
import com.example.own.databinding.DailyWorkoutRowBinding
import java.util.*
import kotlin.collections.ArrayList

class OwnListAdapter(var items:ArrayList<OwnListData>, val today: GregorianCalendar?) : RecyclerView.Adapter<OwnListAdapter.ViewHolder>() {

     interface OnItemClickListener : View.OnClickListener {
         override fun onClick(p0: View?)
     }

    var onItemClickListener: OnItemClickListener?= null

    inner class ViewHolder(val binding: DailyWorkoutRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = DailyWorkoutRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.binding.apply {
            if(items[pos].date.compareTo(today)==0){
                ownListItem.setOnClickListener{onItemClickListener}
            }
//            // text
//            ownListEmoji.setImageResource(R.drawable.ic_baseline_mood_24)

            // emoji
            if(items[pos].isDone){
                when(items[pos].emoji){
//                    0-> {
//                        ownListEmoji.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
//                    }
                    1-> {
                        ownListEmoji.setImageResource(R.drawable.icon_emoji1_background)
                        ownListEmoji.setColorFilter(Color.GREEN)
                    }
                    2-> {
                        ownListEmoji.setImageResource(R.drawable.icon_emoji2_background)
                        ownListEmoji.setColorFilter(Color.YELLOW)
                    }
                    3-> {
                        ownListEmoji.setImageResource(R.drawable.icon_emoji3_background)
                        ownListEmoji.setColorFilter(Color.RED)
                    }
                }
                ownListItem.setBackgroundResource(R.color.own_skyblue_darkBack)
                ownListBodyPart.setBackgroundResource(R.color.own_skyblue_item_done)
                ownListSet.setBackgroundResource(R.color.own_skyblue_item_done)
            }else{
                ownListItem.setBackgroundResource(R.color.own_skyblue_defaultBack)
                ownListBodyPart.setBackgroundResource(R.color.own_yellow)
                ownListSet.setBackgroundResource(R.color.own_yellow)
                ownListEmoji.setColorFilter(Color.BLACK)
                ownListEmoji.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24)
            }
            ownListWorkoutName.text = items[pos].name
            ownListBodyPart.text = items[pos].bodyPart
            ownListSet.text = items[pos].set.toString()+"세트"

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}