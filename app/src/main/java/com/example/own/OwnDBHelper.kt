package com.example.own

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class OwnDBHelper(val context:Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME = "ownDB.db"
        val DB_VERSION =1

        // about achieve table
        val TABLE_NAME = "achieve"
        val LAST_UPDATE ="lastUpdate"
        val OWNWAN_DAYS ="ownwanDays"
    }

    // 데이터 베이스 생성시 테이블 없다면 생성
    override fun onCreate(db: SQLiteDatabase?) {
        val create_table ="create table if not exists $TABLE_NAME(" +
                "$LAST_UPDATE datetime primary key default (datetime('now','localtime')), " +
                "$OWNWAN_DAYS integer);"

        db!!.execSQL(create_table) // SQL에서 실행해라! - 테이블 생성
    }

    public  fun initData(){
        val achieveData = readCurRecord()
        if(achieveData.lastUpdateDate == null){
            insertRecord(0)
        }
    }

    // 테이블 업그레이드 시 실행 내용
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    // 현재 테이블 내용 가져오기 - 반환해야할 것 : 날짜 & ownwanDays (어차피 한줄)
    public fun readCurRecord():AchieveTableData{
        // 질의문으로 데이터 베이스 모든 내용 가져오기
        val strSql = "select * from $TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql,null) // 조건이 없어서 null

        // cursor에서 데이터 읽기
        cursor.moveToFirst()

        if (cursor.count ==0)
            return AchieveTableData(null,0 )

//        Toast.makeText(context, cursor.count.toString(), Toast.LENGTH_LONG).show()
        val lateUpdate=cursor.getString(0)
        val ownwanDays = cursor.getString(1)

        val achieveData = AchieveTableData(lateUpdate, ownwanDays.toInt())

        cursor.close()
        db.close()

        return achieveData
    }

    public fun insertRecord(ownwanDays:Int) : Boolean{
        val values = ContentValues()
//        values.put(LAST_UPDATE, )
        values.put(OWNWAN_DAYS,ownwanDays) // ownwanDays
        val db = writableDatabase

        // 오류나면 -1 반환,
        val flag = db.insert(TABLE_NAME, null, values)>0

        // 삽입 명령어 실행하면 무조건 db.close해야함 (성공 실패 관계 없이)
        db.close()
//        Toast.makeText(context, ownwanDays.toString(), Toast.LENGTH_LONG).show()
        return flag
    }

    // 테이블 내용 업데이트 하기
    public fun updateRecord(ownwanDays:Int){
        val strSql = "delete from $TABLE_NAME;"
        val db = writableDatabase
        val cursor = db.rawQuery(strSql,null)
        Toast.makeText(context, cursor.count.toString(), Toast.LENGTH_LONG).show()
        cursor.close()
        db.close()
        insertRecord(ownwanDays)
    }

}