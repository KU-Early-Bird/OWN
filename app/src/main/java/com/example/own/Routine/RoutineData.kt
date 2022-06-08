package com.example.own.Routine

data class RoutineData(
    var id:Int,
    var name:String,
    var bodyPart:String,
    var setNum:Int,
    var time:Int,
    var restTime:Int,
    var partTime:Int,
    var type:Boolean,
    var sound:Boolean,
    var isDay:Boolean,
    var dayOfWeek:Array<Boolean>,
    var enabled:Boolean
){
    constructor():this(0,"","",
        0,0, 0,0,
        false,false,false, arrayOf(false),false)
}
