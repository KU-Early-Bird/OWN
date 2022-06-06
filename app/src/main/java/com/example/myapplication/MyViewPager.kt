package com.example.myapplication
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyViewPagerAdapter(fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 //viewpager두개 만들거다
    //
    }
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DiaryTabFragment()
            1 -> DiaryWriteFragment()
            else -> DiaryTabFragment()
        }
    }
}
