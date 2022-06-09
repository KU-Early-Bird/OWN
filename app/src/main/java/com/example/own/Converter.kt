package com.example.own

import java.text.SimpleDateFormat
import java.util.*

class Converter {
    val dateFormat=SimpleDateFormat("yyyy-MM-dd")


    public fun convertDayOfWeekToBitSet(date: GregorianCalendar): BitSet {
        val calendar = Calendar.getInstance()
        calendar.time = date.time // 일:1 ~ 토:7
        var bitset = BitSet(7)
        bitset[calendar.get(Calendar.DAY_OF_WEEK)-1] = true
        return bitset
    }

    public fun convertWeekRoutineToBitset(weekRoutine:Int): BitSet {
        var bitset = BitSet(7)// 일월화수목금토 순?
        var wr = weekRoutine
        var i=0
        var str=""
        bitset[0]= true
        bitset[i]
        while (wr>1){
            bitset[i] = (wr%2 == 1)
            i++
            wr/=2
        }
        bitset[i] = wr==1
        return bitset
    }

    fun convertStrToCalender(dateStr:String):GregorianCalendar{
        var dateList = dateStr.split('-')
        return GregorianCalendar(dateList[0].toInt(), dateList[1].toInt(),dateList[2].toInt())
    }

    fun convertCalenderToStr(calendar:GregorianCalendar):String{
        return dateFormat.format(calendar.time)
    }
}