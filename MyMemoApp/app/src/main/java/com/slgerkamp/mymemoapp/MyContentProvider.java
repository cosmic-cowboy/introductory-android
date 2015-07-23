package com.slgerkamp.mymemoapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 *
 */
public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.slgerkamp.mymemoapp.myContentProvider";
    private MyDbHelper myDbHelper;
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + MyMemoContract.Memos.TABLE_NAME);

    private static final int MEMOS = 1;
    private static final int MEMOS_ITEM = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MyMemoContract.Memos.TABLE_NAME, MEMOS);
        uriMatcher.addURI(AUTHORITY, MyMemoContract.Memos.TABLE_NAME + "/#", MEMOS_ITEM);
    }

    @Override
    public boolean onCreate() {
        myDbHelper = new MyDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (uriMatcher.match(uri)){
            case MEMOS :
                break;
            case MEMOS_ITEM :
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                MyMemoContract.Memos.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if(uriMatcher.match(uri) != MEMOS){
            throw new IllegalArgumentException("Unknown URI" + uri);
        }

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        long newId = db.insert(
                MyMemoContract.Memos.TABLE_NAME,
                null,
                values
        );
        Uri newUri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, newId);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if(uriMatcher.match(uri) != MEMOS_ITEM){
            throw new IllegalArgumentException("Unknown URI" + uri);
        }

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        int count = db.delete(
                MyMemoContract.Memos.TABLE_NAME,
                selection,
                selectionArgs
        );

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if(uriMatcher.match(uri) != MEMOS_ITEM){
            throw new IllegalArgumentException("Unknown URI" + uri);
        }

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        int count = db.update(
                MyMemoContract.Memos.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
