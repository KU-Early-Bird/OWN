package com.example.routinetab

data class RoutineData(
    var name:String,
    var bodyPart:String,
    var setNum:Int,
    var count:Int,
    var time:Int,
    var restTime:Int,
    var partTime:Int,
    var type:Boolean,
    var sound:Boolean,
    var isDay:Boolean,
    var dayOfWeek:BooleanArray
)
