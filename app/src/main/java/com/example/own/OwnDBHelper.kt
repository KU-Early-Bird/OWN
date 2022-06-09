package com.example.own

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import com.example.own.Diary.DiaryData
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
        val WORKOUT_EMOJI_ID = "EMOJI_ID"
        val WORKOUT_DURATION = "DURATION"
        val WORKOUT_NAME ="NAME"
        val WORKOUT_BODY_PART = "BODY_PART"
        val WORKOUT_IS_DONE = "IS_DONE"
        val WORKOUT_SET = "WORKOUT_SET"
    }

    val dateFormat = SimpleDateFormat("yyyy-M-d")
    val workoutDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val tempDateFormat = SimpleDateFormat("yyyy.MM.dd")

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
                "$WORKOUT_SET integer," +
                "$WORKOUT_DURATION text,"+
                "$WORKOUT_EMOJI_ID integer,"+
                "$WORKOUT_IS_DONE integer);"

        db!!.execSQL(create_workout_table)
        println("workout table had been made")
    }


    // 테이블 업그레이드 시 실행 내용
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val drop_table = "drop table if exists $ACHIEVE_TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    // 현재 테이블 내용 가져오기 - 반환해야할 것 : 날짜 & ownwanDays (어차피 한줄)
    public fun readAchieve():AchieveTableData{
        // 질의문으로 데이터 베이스 모든 내용 가져오기
        val strSql = "select * from $ACHIEVE_TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql,null) // 조건이 없어서 null

        // cursor에서 데이터 읽기
        cursor.moveToFirst()

        if (cursor.count ==0)
            return AchieveTableData(null,0,false )

//        Toast.makeText(context, cursor.count.toString(), Toast.LENGTH_LONG).show()
        val lateUpdate=converter.convertStrToCalender(cursor.getString(0))
        val ownwanDays = cursor.getString(1).toInt()
        val didComplete = getDidComplete(cursor.getString(2).toInt())


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
        val dateStr = dateFormat.format(GregorianCalendar(day.year,day.month-1,day.day).time)
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
        val dateStr = dateFormat.format(day.time)
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
        val dateStr = dateFormat.format(day.time)
        val strSql = "select $WORKOUT_DATE, $WORKOUT_IS_DONE,$WORKOUT_NAME,$WORKOUT_BODY_PART, " +
                "${WORKOUT_SET}, ${WORKOUT_EMOJI_ID} " +
                "from $WORKOUT_TABLE_NAME " +
                "where $WORKOUT_DATE = '$dateStr';"

        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)

        cursor.moveToFirst()
        if(cursor.count>0){
            do {
                val calendar = converter.convertStrToCalender(cursor.getString(0))
                val isDone = cursor.getString(1).toInt()==1
                val name = cursor.getString(2)
                val bodyPart = cursor.getString(3)
                val set = cursor.getString(4).toInt()
                val emoji= cursor.getString(5).toInt()

                var ownListData = OwnListData(calendar,isDone,name,bodyPart,set,emoji)
                ownList.add(ownListData)
            }while(cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return ownList
    }

    public fun getRoutineOwnList(day: GregorianCalendar):ArrayList<OwnListData>{
        val ownList = ArrayList<OwnListData>()
        val dateStr = dateFormat.format(day.time)
//        var strSql = "select $WORKOUT_DATE, $WORKOUT_IS_DONE,$WORKOUT_NAME,$WORKOUT_BODY_PART, " +
//                "${WORKOUT_SET} from $ROUTINE_TABLE_NAME;"
//        val db = readableDatabase
//        val cursor = db.rawQuery(strSql, null)
//
//        cursor.moveToFirst()
//        if(cursor.count>0){
//            do {
//
////
////                 val name=cursor.getString(1)
//////                 val bodyPart=cursor.getString(3)
////                 val set=cursor.getString(4).toInt()
//                    val dayOfWeek = getString(4).toInt()
//                    val isDay =cursor.getString(4).toInt() == 1

        // 루틴에서 받아온 week 값 -> 비트맵 변환
        // 입력 날짜에서 받아온 week 값 -> 비트맵 변환
        // 두 비트맵 AND 연산
        // 연산한게 1이면 그 날 루틴인 것 -> ownList에 추가


////                var ownListData =
////                    OwnListData(cursor.getString(1), cursor.getString(2), cursor.getString(3))
////                ownList.add(ownListData)
//            }while(cursor.moveToNext())
//        }
//
//        cursor.close()
//        db.close()
//
        return ownList
      }

    //val create_routine_table = "create table if not exists $ROUTINE_TABLE_NAME("+
    //                "$ID integer primary key autoincrement, "+
    //                "$NAME varchar, "+
    //                "$BODYPART varchar, "+
    //                "$SETNUM int, "+
    //                "$TIME int, "+
    //                "$RESTTIME int, "+
    //                "$PARTTIME int, "+
    //                "$TYPE tinyint, "+
    //                "$SOUND tinyint, "+
    //                "$ISDAY tinyint, "+
    //                "$DAYOFWEEK tinyint, "+
    //                "$ENABLED tinyint);"
    //        db!!.execSQL(create_routine_table)





}