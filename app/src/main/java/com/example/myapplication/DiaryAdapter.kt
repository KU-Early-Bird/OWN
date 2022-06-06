package com.example.myapplication

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.DiaryRowBinding
import com.example.myapplication.databinding.FragmentDiaryTabBinding

class DiaryAdapter(var diaryLists:ArrayList<DiaryData>) : RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {
//the ArrayList of item
    interface OnItemClickListener{//will be embody on the caller fragment
        fun onItemClick(data:DiaryData)
    }
    var itemClickListener:OnItemClickListener?=null

    //binding to Rows of Recyclerview
    inner class DiaryViewHolder(val binding:DiaryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init{
            //give clicklistener to each view of row
            binding.DiaryGalleryImage.setOnClickListener{
                itemClickListener?.onItemClick(diaryLists[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        //bindng initialize to row
        val binding =DiaryRowBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return  DiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        //set each views of row with position and ArrayList which this Adapter received
        var bitmap = BitmapFactory.decodeFile(diaryLists[position].Diary_Image)
        holder.binding.DiaryGalleryImage.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return diaryLists.size
    }

}