package com.example.own

import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class CurrentDayDecorator(private val context: FragmentActivity?):DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == CalendarDay.today()
    }

    override fun decorate(view: DayViewFacade?) {
        if (view != null) {
            view.setBackgroundDrawable(AppCompatResources.getDrawable(context!!,R.drawable.ic_baseline_panorama_fish_eye_24)!!)
        }
    }
}