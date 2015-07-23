package com.slgerkamp.mymemoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class MyDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "mymemo.db";
    public static final int DB_VERSION = 1;

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyMemoContract.Memos.CREATE_TABLE);
        db.execSQL(MyMemoContract.Memos.INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyMemoContract.Memos.DROP_TABLE);
        onCreate(db);
    }
}
