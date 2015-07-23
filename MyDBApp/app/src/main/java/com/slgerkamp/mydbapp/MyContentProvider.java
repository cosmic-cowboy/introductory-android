package com.slgerkamp.mydbapp;

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
public class MyContentProvider extends ContentProvider{

    private static final String AUTHORITY ="com.slgerkamp.mydbapp.MyContentProvider";
    static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + MyAppContract.Users.TABLE_NAME);

    // UriMatcher
    private static final int USERS = 1;
    private static final int USERS_ITEM = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MyAppContract.Users.TABLE_NAME, USERS);
        uriMatcher.addURI(AUTHORITY, MyAppContract.Users.TABLE_NAME + "/#", USERS_ITEM);
    }

    MyDbHelper myDbHelper;

    @Override
    public boolean onCreate() {
        myDbHelper = new MyDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(uriMatcher.match(uri) != USERS){
            throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                MyAppContract.Users.TABLE_NAME,
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
        if(uriMatcher.match(uri) != USERS){
            throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        long newId = db.insert(
                MyAppContract.Users.TABLE_NAME,
                null,
                values
        );
        Uri newUri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, newId);
        getContext().getContentResolver().notifyChange(uri, null);
        return newUri;
     }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if(uriMatcher.match(uri) != USERS_ITEM){
            throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        int count = db.delete(
                MyAppContract.Users.TABLE_NAME,
                selection,
                selectionArgs
        );
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(uriMatcher.match(uri) != USERS_ITEM){
            throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        int count = db.update(
                MyAppContract.Users.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
