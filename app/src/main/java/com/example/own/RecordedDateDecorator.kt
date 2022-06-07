package com.example.own

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class RecordedDateDecorator(val days:ArrayList<CalendarDay>) :DayViewDecorator{
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return days.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        if (view != null) {
            view.addSpan(DotSpan(5f, Color.parseColor("#FFF8BB48")))
        }
    }
}