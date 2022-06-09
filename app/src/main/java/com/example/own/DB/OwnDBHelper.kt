package com.example.own.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.own.CalendarFragment
import com.example.own.Others.Converter
import java.util.*
import com.example.own.Diary.DiaryData
import com.example.own.Home.AchieveTableData
import com.example.own.Home.OwnListData
import com.example.own.Routine.RoutineData
import com.example.own.Workout.WorkoutData
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlin.collections.ArrayList


class OwnDBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    val converter= Converter()
    companion object{
        val DB_NAME = "ownDB.db"
        val DB_VERSION =1

        // about achieve table
        val ACHIEVE_TABLE_NAME = "achieve"
        val LAST_UPDATE ="lastUpdate"
        val OWNWAN_DAYS ="ownwanDays"
        val DID_COMPLETE = "didComplete"

        val DIARY_TABLE_NAME = "Diary"
        val DIARYDATE = "DIARY_DATE"
        val DIARYCONTENT = "DIARY_CONTENT"
        val DIARYIMAGE = "DIARY_IMAGE"

        //workout table
        val WORKOUT_TABLE_NAME = "WORKOUT"
        val WORKOUT_DATE = "DATE"
        val WORKOUT_ASSESSMENT = "ASSESSMENT"
        val WORKOUT_TYPE = "TYPE"
        val WORKOUT_EMOJI_ID = "EMOJI_ID"
        val WORKOUT_DURATION = "DURATION"
        val WORKOUT_NAME ="NAME"
        val WORKOUT_RESTTIME = "resttime"
        val WORKOUT_PARTTIME = "parttime"
        val WORKOUT_BODY_PART = "BODY_PART"
        val WORKOUT_IS_DONE = "IS_DONE"
        val WORKOUT_SET = "WORKOUT_SET"
        
        //routine table
        val ROUTINE_TABLE_NAME = "routine"
        val ID = "id"
        val NAME = "name"
        val BODYPART = "bodypart"
        val SETNUM = "setnum"
        val TIME = "time"
        val RESTTIME = "resttime"
        val PARTTIME = "parttime"
        val TYPE = "type"
        val SOUND = "sound"
        val ISDAY = "isday"
        val DAYOFWEEK = "dayofweek"
        val ENABLED = "enabled"
    }

//    val dateFormat = SimpleDateFormat("yyyy-M-d")

    lateinit var DiaryList:ArrayList<DiaryData>
    var diaryrowcount=-1
    init {
        DiaryList=ArrayList<DiaryData>()
    }


    // 데이터 베이스 생성시 테이블 없다면 생성
    override fun onCreate(db: SQLiteDatabase?) {

        var create_achieve_table ="create table if not exists $ACHIEVE_TABLE_NAME(" +
                "$LAST_UPDATE date primary key default (date('now')), " +
                "$OWNWAN_DAYS integer," +
                "$DID_COMPLETE tinyint );"

        db!!.execSQL(create_achieve_table) // SQL에서 실행해라! - 테이블 생성

        var create_diary_table = "create table if not exists $DIARY_TABLE_NAME(" +
                "DID integer primary key autoincrement, " +
                "$DIARYDATE text, " +
                "$DIARYCONTENT text, " +
                "$DIARYIMAGE text);"
        db!!.execSQL(create_diary_table)
        Log.e("db" , "table had been made")

        var create_workout_table ="create table if not exists $WORKOUT_TABLE_NAME(" +
                "WID integer primary key autoincrement,"+
                "$WORKOUT_DATE date default (date('now')), " +
                "$WORKOUT_NAME text,"+
                "$WORKOUT_BODY_PART text,"+
                "$WORKOUT_ASSESSMENT text," +
                "$WORKOUT_TYPE tinyint, "+
                "$WORKOUT_SET integer," +
                "$WORKOUT_RESTTIME int, "+
                "$WORKOUT_PARTTIME int, "+
                "$WORKOUT_DURATION text,"+
                "$WORKOUT_EMOJI_ID integer,"+
                "$WORKOUT_IS_DONE integer);"

        db!!.execSQL(create_workout_table)
        println("workout table had been made")
        
        val create_routine_table = "create table if not exists $ROUTINE_TABLE_NAME("+
                "$ID integer primary key autoincrement, "+
                "$NAME varchar, "+
                "$BODYPART varchar, "+
                "$SETNUM int, "+
                "$TIME int, "+
                "$RESTTIME int, "+
                "$PARTTIME int, "+
                "$TYPE tinyint, "+
                "$SOUND tinyint, "+
                "$ISDAY tinyint, "+
                "$DAYOFWEEK tinyint, "+
                "$ENABLED tinyint);"
        db!!.execSQL(create_routine_table)
    }


    // 테이블 업그레이드 시 실행 내용
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val drop_table = "drop table if exists $ACHIEVE_TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    // 현재 테이블 내용 가져오기 - 반환해야할 것 : 날짜 & ownwanDays (어차피 한줄)
    public fun readAchieve(): AchieveTableData {
        // 질의문으로 데이터 베이스 모든 내용 가져오기
        val strSql = "select * from $ACHIEVE_TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql,null) // 조건이 없어서 null

        // cursor에서 데이터 읽기
        cursor.moveToFirst()

        if (cursor.count ==0)
            return AchieveTableData(null,0,false )

        val lateUpdate=converter.convertStrToCalender(cursor.getString(0))
        val ownwanDays = cursor.getString(1).toInt()
        val didComplete = getDidComplete(cursor.getString(2).toInt())
        Toast.makeText(context, cursor.getString(0), Toast.LENGTH_LONG).show()


        val achieveData = AchieveTableData(lateUpdate, ownwanDays, didComplete)

        cursor.close()
        db.close()

        return achieveData
    }

    private fun getDidComplete(flag:Int):Boolean{
        return flag == 1
    }


    public fun insertRecord(ownwanDays:Int,didComplete:Int) : Boolean{
        val values = ContentValues()

        values.put(LAST_UPDATE,converter.convertCalenderToStr(CalendarFragment().getTodayGregorian()))
        values.put(OWNWAN_DAYS,ownwanDays) // ownwanDays
        values.put(DID_COMPLETE,didComplete)

        val db = writableDatabase

        // 오류나면 -1 반환,
        val flag = db.insert(ACHIEVE_TABLE_NAME, null, values)>0

        // 삽입 명령어 실행하면 무조건 db.close해야함 (성공 실패 관계 없이)
        db.close()
//        Toast.makeText(context, ownwanDays.toString(), Toast.LENGTH_LONG).show()
        return flag
    }

    public fun deleteAchieve(){
        val strSql = "delete from $ACHIEVE_TABLE_NAME;"
        val db = writableDatabase
        var flag = db.delete(ACHIEVE_TABLE_NAME,null,null)
        if(flag==-1)
            Toast.makeText(context,"fail",Toast.LENGTH_SHORT).show()

        db.close()
    }

    // 테이블 내용 업데이트 하기
    public fun updateAchieve(ownwanDays:Int,didComplete: Boolean){
        deleteAchieve()
        var didCompleteFlag = 0
        if(didComplete) didCompleteFlag=1 else didCompleteFlag=0
        insertRecord(ownwanDays,didCompleteFlag)
    }


    ////////// Diary

    fun getAllDiary(): ArrayList<DiaryData> {
        DiaryList = ArrayList<DiaryData>()
        val strsql ="select * from $DIARY_TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)

        //how many rows in table ,테이블에 줄이 몇개있는지 받아오는거
        //SELECT COUNT(index number) FROM $tablename;
        val c = db!!.rawQuery("SELECT COUNT(DID) FROM $DIARY_TABLE_NAME;", null)
        c.moveToFirst()
        diaryrowcount = c.getString(0).toInt()

        //diarylist initialize
        cursor.moveToFirst()
        val attrcount = cursor.columnCount
        //table의 각 줄을 받아오는거 줄이 1개 이상있는경우
        if(diaryrowcount>0)
            do {
                //oncreate 에서 테이블 만든 순서대로 columnindex 정해짐 0 > DID / 1 > DATE / 2 > CONTENT / 3 > IMAGE(path)
                var diary_item =
                    DiaryData(cursor.getString(1), cursor.getString(2), cursor.getString(3))
                DiaryList.add(diary_item)
            }while(cursor.moveToNext())

        cursor.close()
        db.close()

        return DiaryList
    }


    fun insertDiaryData(newdiarydata: DiaryData){
        DiaryList.add(newdiarydata)
        val values = ContentValues()
        //put data into row
        //values.put(column_name, data)
        values.put(DIARYDATE, newdiarydata.Diary_Date)
        values.put(DIARYCONTENT, newdiarydata.Diary_Content)
        values.put(DIARYIMAGE, newdiarydata.Diary_Image)

        val db = writableDatabase
        //result for input testing
        var result = db.insert(DIARY_TABLE_NAME, null, values)

        if(result == (-1).toLong())
            Toast.makeText(context,"Failed", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context,"Success", Toast.LENGTH_LONG).show()
        db.close()
    }

    // 해당 날짜 기록데이터 반환
    fun getDiaryData(day: CalendarDay):DiaryData?{
        val dateStr = converter.convertCalenderToStr(GregorianCalendar(day.year,day.month-1,day.day))
        var strSql = "select * from $DIARY_TABLE_NAME where $DIARYDATE = '$dateStr';"
        val db = readableDatabase

        val cursor  = db.rawQuery(strSql,null)
        cursor.moveToFirst()

        var hasData = cursor.count != 0
        // 0 > DID / 1 > DATE / 2 > CONTENT / 3 > IMAGE(path)
        var diary_item:DiaryData ?= null
        if(hasData)
            diary_item =
                DiaryData(cursor.getString(1), cursor.getString(2), cursor.getString(3))

        // 기록이 있을 경우 true 반환
        cursor.close()
        db.close()

        return diary_item
    }

    fun getDiaryData(day: GregorianCalendar):DiaryData?{
        val dateStr = converter.convertCalenderToStr(day)
        var strSql = "select * from $DIARY_TABLE_NAME where $DIARYDATE = '$dateStr';"
        val db = readableDatabase

        val cursor  = db.rawQuery(strSql,null)
        cursor.moveToFirst()

        var hasData = cursor.count != 0
        // 0 > DID / 1 > DATE / 2 > CONTENT / 3 > IMAGE(path)
        var diary_item:DiaryData ?= null
        if(hasData)
            diary_item =
                DiaryData(cursor.getString(1), cursor.getString(2), cursor.getString(3))

        // 기록이 있을 경우 true 반환
        cursor.close()
        db.close()

        return diary_item
    }


    /*Workout data*/
    // 날짜 넣으면 그날 데이터 반환하기 ownList형태로
    public fun getWorkoutOwnList(day: GregorianCalendar):ArrayList<OwnListData>{
        val ownList = ArrayList<OwnListData>()
        val dateStr = converter.convertCalenderToStr(day)
        val strSql = "select $WORKOUT_DATE, $WORKOUT_IS_DONE,$WORKOUT_NAME,$WORKOUT_BODY_PART, $WORKOUT_SET, $WORKOUT_EMOJI_ID " +
                "from $WORKOUT_TABLE_NAME " +
                "where $WORKOUT_DATE = '$dateStr';"

        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)

        cursor.moveToFirst()
        if(cursor.count>0){
            do {
                val calendar = converter.convertStrToCalender(cursor.getString(cursor.getColumnIndex(WORKOUT_DATE).toInt()))
                val isDone = cursor.getString(cursor.getColumnIndex(WORKOUT_IS_DONE).toInt()).toInt()==1
                val name = cursor.getString(cursor.getColumnIndex(WORKOUT_NAME).toInt())
                val bodyPart = cursor.getString(cursor.getColumnIndex(WORKOUT_BODY_PART).toInt())
                val set = cursor.getString(cursor.getColumnIndex(WORKOUT_SET).toInt()).toInt()
                val emoji= cursor.getString(cursor.getColumnIndex(WORKOUT_EMOJI_ID).toInt()).toInt()

                var ownListData = OwnListData(calendar,isDone,name,bodyPart,set,emoji)
                ownList.add(ownListData)
            }while(cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ownList
    }

    // 현재 테이블 내용 가져오기 - 반환해야할 것 : 날짜 & ownwanDays (어차피 한줄)
    public fun getWorkoutList(calendar: GregorianCalendar):ArrayList<WorkoutData>{
        // 질의문으로 데이터 베이스 모든 내용 가져오기
        val workoutList = ArrayList<WorkoutData>()
        val dateStr = converter.convertCalenderToStr(calendar)
        Log.d("date",dateStr)
        val strSql = "select * from $WORKOUT_TABLE_NAME where $WORKOUT_DATE = '$dateStr';"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql,null) // 조건이 없어서 null

        // cursor에서 데이터 읽기
        cursor.moveToFirst()

        Log.d("cursor",cursor.count.toString())

        if(cursor.count>0){
            do {
                val isDone = cursor.getString(cursor.getColumnIndex(WORKOUT_IS_DONE).toInt()).toInt()==1
                if(isDone){
                    val name = cursor.getString(cursor.getColumnIndex(WORKOUT_NAME).toInt())
                    val bodyPart = cursor.getString(cursor.getColumnIndex(WORKOUT_BODY_PART).toInt())
                    val set = cursor.getString(cursor.getColumnIndex(WORKOUT_SET).toInt()).toInt()
                    val emoji = cursor.getString(cursor.getColumnIndex(WORKOUT_EMOJI_ID).toInt()).toInt()
                    val assessment=cursor.getString(cursor.getColumnIndex(WORKOUT_ASSESSMENT).toInt())

                    var workoutData=WorkoutData(id =0,date = dateStr,workoutName = name,
                        bodyPart=bodyPart, assessment = assessment,count=0,restTime=0,partTime=0 ,
                        set=set,duration="", emojiID = emoji, isDone = true, type = 0, isSoundOn=false )
                    workoutList.add(workoutData)

                }

            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return workoutList
    }


    public fun getRoutineWorkoutList(day: GregorianCalendar):ArrayList<WorkoutData>{
        val workoutList = ArrayList<WorkoutData>()
        var strSql = "select *  from $ROUTINE_TABLE_NAME"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)

        cursor.moveToFirst()
        if(cursor.count>0){
            do {
                val enabled=cursor.getString(0).toInt()==1
                if(!enabled) continue

                val isDay = cursor.getString(1).toInt()==1
                if(!isDay)continue

                val weekRoutine = converter.convertWeekRoutineToBitset(cursor.getString(1).toInt())
                weekRoutine.and(converter.convertDayOfWeekToBitSet(day))
                var flag = false
                for(i in 0 until 7){
                    if(weekRoutine[i]){
                        flag=true
                        break
                    }
                }

                if(!flag) continue


                val id = cursor.getString(cursor.getColumnIndex(ID).toInt()).toInt()
                val name = cursor.getString(cursor.getColumnIndex(NAME).toInt())
                val bodyPart = cursor.getString(cursor.getColumnIndex(BODYPART).toInt())
                val set = cursor.getString(cursor.getColumnIndex(SETNUM).toInt()).toInt()
                val count = cursor.getString(cursor.getColumnIndex(TIME).toInt()).toInt()
                val restTime = cursor.getString(cursor.getColumnIndex(RESTTIME).toInt()).toInt()
                val partTime = cursor.getString(cursor.getColumnIndex(PARTTIME).toInt()).toInt()
                val type = cursor.getString(cursor.getColumnIndex(TYPE).toInt()).toInt()
                val isSoundOn=cursor.getString(cursor.getColumnIndex(SOUND).toInt()).toInt()==1
                val dateStr= converter.convertCalenderToStr(day)

                var workoutData=WorkoutData(id =id,date = dateStr,workoutName = name,
                    bodyPart=bodyPart, assessment = "",count=count,restTime=restTime,partTime=partTime ,
                    set=set,duration="", emojiID = 0, isDone = false, type = type, isSoundOn=isSoundOn )
                workoutList.add(workoutData)
            }while(cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return workoutList
      }

    public fun deleteWorkout(calendar: GregorianCalendar){
        val dateStr = converter.convertCalenderToStr(calendar)
        val strSql = "delete from $WORKOUT_TABLE_NAME where $WORKOUT_DATE = $dateStr;"
        val db = writableDatabase
        var flag = db.delete(WORKOUT_TABLE_NAME,null,null)
        if(flag==-1)
            Toast.makeText(context,"fail",Toast.LENGTH_SHORT).show()
        db.close()
    }

    public fun insertWorkout(workoutData: WorkoutData) : Boolean{
        val values = ContentValues()
        values.put(WORKOUT_DATE,workoutData.date)
        values.put(WORKOUT_ASSESSMENT,workoutData.assessment)
        values.put(WORKOUT_TYPE,workoutData.type)
        values.put(WORKOUT_EMOJI_ID,workoutData.emojiID)
        values.put(WORKOUT_DURATION,workoutData.duration)
        values.put(WORKOUT_NAME,workoutData.workoutName)
        values.put(WORKOUT_RESTTIME,workoutData.restTime)
        values.put(WORKOUT_PARTTIME,workoutData.partTime)
        values.put(WORKOUT_BODY_PART,workoutData.bodyPart)
        values.put(WORKOUT_IS_DONE,workoutData.isDone)
        values.put(WORKOUT_SET,workoutData.set)

        val db = writableDatabase

        // 오류나면 -1 반환,
        val flag = db.insert(WORKOUT_TABLE_NAME, null, values)>0
        db.close()
        return flag
    }



    ////////////// Routine
    fun getAllRoutine(): ArrayList<RoutineData> {
        var rtData = ArrayList<RoutineData>()
        val strsql = "select * from $ROUTINE_TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        cursor.moveToFirst()
        val attrcount = cursor.columnCount
        if(cursor.count != 0) {
            do {
                var id = cursor.getString(cursor.getColumnIndex("id").toString().toInt()).toInt()
                var name = cursor.getString(cursor.getColumnIndex("name").toString().toInt())
                var bodyPart = cursor.getString(cursor.getColumnIndex("bodypart").toString().toInt())
                var setNum = cursor.getString(cursor.getColumnIndex("setnum").toString().toInt()).toInt()
                var time = cursor.getString(cursor.getColumnIndex("time").toString().toInt()).toInt()
                var restTime = cursor.getString(cursor.getColumnIndex("resttime").toString().toInt()).toInt()
                var partTime = cursor.getString(cursor.getColumnIndex("parttime").toString().toInt()).toInt()
                var type = cursor.getString(cursor.getColumnIndex("type").toString().toInt()).toInt() == 0
                var sound = cursor.getString(cursor.getColumnIndex("sound").toString().toInt()).toInt() == 0
                var isDay = cursor.getString(cursor.getColumnIndex("isday").toString().toInt()).toInt() == 0
                var dayofweek = cursor.getString(cursor.getColumnIndex("dayofweek").toString().toInt()).toInt()
                var dayOfWeek = BooleanArray(7)
                for (j in 6 downTo 0) {
                    dayOfWeek[j] =
                        if (dayofweek % 2 == 0) false
                        else true
                    Log.d("rtdatacheck",j.toString() + " ::::: " + dayOfWeek[j])
                    dayofweek = dayofweek / 2
                }
                var enabled = cursor.getString(cursor.getColumnIndex("enabled").toString().toInt()).toInt() == 0
                rtData.add(
                    RoutineData(id, name, bodyPart, setNum, time, restTime, partTime, type, sound, isDay, dayOfWeek, enabled)
                )


            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return rtData
    }

    fun findRoutine(id:Int): RoutineData {
        var rtData = RoutineData()
        rtData.id = id
        val strsql = "select * from $ROUTINE_TABLE_NAME where $ID='$id';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        cursor.moveToFirst()
        rtData.name = cursor.getString(cursor.getColumnIndex("name").toString().toInt())
        rtData.bodyPart = cursor.getString(cursor.getColumnIndex("bodypart").toString().toInt())
        rtData.setNum = cursor.getString(cursor.getColumnIndex("setnum").toString().toInt()).toInt()
        rtData.time = cursor.getString(cursor.getColumnIndex("time").toString().toInt()).toInt()
        rtData.restTime = cursor.getString(cursor.getColumnIndex("resttime").toString().toInt()).toInt()
        rtData.partTime = cursor.getString(cursor.getColumnIndex("parttime").toString().toInt()).toInt()
        rtData.type = cursor.getString(cursor.getColumnIndex("type").toString().toInt()).toInt() == 0
        rtData.sound = cursor.getString(cursor.getColumnIndex("sound").toString().toInt()).toInt() == 0
        rtData.isDay = cursor.getString(cursor.getColumnIndex("isday").toString().toInt()).toInt() == 0
        var dayofweek = cursor.getString(cursor.getColumnIndex("dayofweek").toString().toInt()).toInt()
        for(j in 6 downTo 0) {
            rtData.dayOfWeek[j] = dayofweek % 2 == 0
            dayofweek = dayofweek / 2
        }
        rtData.enabled = cursor.getString(cursor.getColumnIndex("enabled").toString().toInt()).toInt() == 0
        cursor.close()
        db.close()
        return rtData
    }

    fun insertRoutine(rtData: RoutineData):Boolean{
        val values = ContentValues()
        values.put(NAME,rtData.name)
        values.put(BODYPART,rtData.bodyPart)
        values.put(SETNUM,rtData.setNum)
        values.put(TIME,rtData.time)
        values.put(RESTTIME,rtData.restTime)
        values.put(PARTTIME,rtData.partTime)
        values.put(TYPE, if(rtData.type) 1 else 0)
        values.put(SOUND, if(rtData.sound) 1 else 0)
        values.put(ISDAY, if(rtData.isDay) 1 else 0)
        var dayofweek = 0
        for(j in 6 downTo 0){
            dayofweek *= 2
            if(rtData.dayOfWeek[j])
                dayofweek += 1
        }
        values.put(DAYOFWEEK, dayofweek)
        values.put(ENABLED, if(rtData.enabled) 1 else 0)
        val db = writableDatabase
        val flag = db.insert(ROUTINE_TABLE_NAME, null, values)>0
        db.close()
        return flag
    }

    fun updateRoutine(rtData: RoutineData): Boolean {
        val id = rtData.id
        val strsql = "select * from $ROUTINE_TABLE_NAME where $ID='$id';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(ID, rtData.id)
            values.put(NAME,rtData.name)
            values.put(BODYPART,rtData.bodyPart)
            values.put(SETNUM,rtData.setNum)
            values.put(TIME,rtData.time)
            values.put(RESTTIME,rtData.restTime)
            values.put(PARTTIME,rtData.partTime)
            values.put(TYPE, if(rtData.type) 1 else 0)
            values.put(SOUND, if(rtData.sound) 1 else 0)
            values.put(ISDAY, if(rtData.isDay) 1 else 0)
            var dayofweek = 0
            for(j in 6 downTo 0){
                if(rtData.dayOfWeek[j])
                    dayofweek += 1
                dayofweek *= 2
            }
            values.put(DAYOFWEEK, dayofweek)
            values.put(ENABLED, if(rtData.enabled) 1 else 0)
            db.update(ROUTINE_TABLE_NAME, values, "$ID=?", arrayOf(id.toString()))
        }
        cursor.close()
        db.close()
        return flag
    }
}