package com.slgerkamp.mydbapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 *
 */
public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "myapp.db";
    private static final int DB_VERSION = 2;

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyAppContract.Users.CREATE_TABLE);
        db.execSQL(MyAppContract.Users.INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyAppContract.Users.DROP_TABLE);
        onCreate(db);
    }
}
