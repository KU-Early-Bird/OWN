package com.example.own.Home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.FragmentActivity
import com.example.own.R


class LevelGridAdapter(val context: FragmentActivity?, val levels:ArrayList<Int>): BaseAdapter() {

    override fun getCount(): Int {
        return levels.size
    }

    override fun getItem(p0: Int): Any {
        return levels[p0];
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(i: Int, view: View?, parent: ViewGroup?): View {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val levelView = inflater.inflate(R.layout.level_grid,null)
        if (levels[i]==0)
            levelView.visibility = View.INVISIBLE
        return levelView
    }
}