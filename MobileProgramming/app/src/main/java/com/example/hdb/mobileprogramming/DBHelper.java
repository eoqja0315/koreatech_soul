package com.example.hdb.mobileprogramming;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBName = "userdata.db";
    private static final int DBVer = 2;

    public DBHelper(Context context) {
        super(context, DBName, null, DBVer);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE userinfo(_id TEXT, _date TEXT, _category TEXT, _breakdown TEXT, _money TEXT, _method TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userinfo");
        onCreate(db);
    }
}
