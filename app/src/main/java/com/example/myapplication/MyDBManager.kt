package com.example.myapplication
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DiaryTabFragment

class MyDBManager(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME="ownDB.db"
        //or own.db?
        val DB_VERSION=1
        val DIARY_TABLE_NAME = "Diary"
        val DIARYDATE = "DIARY_DATE"
        val DIARYCONTENT = "DIARY_CONTENT"
        val DIARYIMAGE = "DIARY_IMAGE"
    }
    lateinit var DiaryList:ArrayList<DiaryData>
    var diaryrowcount = -1
    init {
        DiaryList = ArrayList<DiaryData>()
    }

    fun getAllDiary(): ArrayList<DiaryData> {
        val strsql ="select * from $DIARY_TABLE_NAME;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)

        //how many rows in table ,테이블에 줄이 몇개있는지 받아오는거
        //SELECT COUNT(index number) FROM $tablename;
        val c = db!!.rawQuery("SELECT COUNT(DID) FROM $DIARY_TABLE_NAME;", null)
        c.moveToFirst()
        diaryrowcount = c.getString(0).toInt()

        DiaryListRefresh(cursor)
        cursor.close()
        db.close()
        return DiaryList
    }

    private fun DiaryListRefresh(cursor:Cursor) {
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
    }


    override fun onCreate(db: SQLiteDatabase?) {
        //create diary table if not exist
        val create_table ="create table if not exists $DIARY_TABLE_NAME(" +
                "DID integer primary key autoincrement, " +
                "$DIARYDATE text, " +
                "$DIARYCONTENT text, " +
                "$DIARYIMAGE text);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
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
}