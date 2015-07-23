package com.slgerkamp.mymemoapp;

import android.provider.BaseColumns;

/**
 * テーブル情報
 */
public class MyMemoContract {

    public MyMemoContract() {}

    public static abstract class Memos implements BaseColumns {
        public static final String TABLE_NAME = "memos";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_CREATED = "created";
        public static final String COLUMN_UPDATED = "updated";


        public static final String CREATE_TABLE =
                "create table " + TABLE_NAME + " (" +
                        COLUMN_ID + " integer primary key autoincrement, " +
                        COLUMN_TITLE + " text, " +
                        COLUMN_BODY + " text, " +
                        COLUMN_CREATED + " datetime default current_timestamp, " +
                        COLUMN_UPDATED + " datetime default current_timestamp)";
        public static final String INIT_TABLE =
                "insert into memos (title, body) values ('title1', 'body1'), ('title2', 'body2')";
        public static final String DROP_TABLE =
                "drop table if exists " + TABLE_NAME;

    }
}
