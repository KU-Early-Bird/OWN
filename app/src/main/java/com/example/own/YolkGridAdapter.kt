package com.example.own

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity


class YolkGridAdapter(val context: FragmentActivity?, val yolks:ArrayList<Int>): BaseAdapter() {
    override fun getCount(): Int {
        return yolks.size
    }

    override fun getItem(p0: Int): Any {
        return yolks[p0];
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(i: Int, view: View?, parent: ViewGroup?): View {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val yolkView = inflater.inflate(R.layout.yolk_grid,null)
        if (yolks[i]==1)
            yolkView.findViewById<ImageView>(R.id.yolk).setImageResource(R.drawable.yolk)
        else
            yolkView.findViewById<ImageView>(R.id.yolk).setImageResource(R.drawable.gray)
        return yolkView
    }
}