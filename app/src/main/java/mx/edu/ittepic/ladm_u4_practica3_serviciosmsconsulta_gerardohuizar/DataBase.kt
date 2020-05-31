package mx.edu.ittepic.ladm_u4_practica3_serviciosmsconsulta_gerardohuizar

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase (context:Context?,
                name:String?,
                factory: SQLiteDatabase.CursorFactory?,
                version: Int): SQLiteOpenHelper(context,name,factory,version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE INBOX(NAME VARCHAR(200), LASTNAME VARCHAR(200) , AGE VARCHAR(3) , CURP VARCHAR(10), HISTORY VARCHAR(2000))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}