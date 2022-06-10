package com.example.own.Workout

data class WorkoutData (
    var id:Int,
    var date:String,
    var workoutName: String,
    var bodyPart:String,
    var assessment: String,
    var set:Int,
    var count:Int,
    var duration:String,
    var restTime:Int,
    var partTime:Int,
    var emojiID: Int,
    var isDone:Boolean,
    var type:Int,
    var isClicked:Boolean,
    var progressBarCount: Int,
    var progressBarCountText: String,
    var progressBarSet:Int,
    var progressBarSetText:String
)