package com.slgerkamp.mydbapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DBのOpen
        MyDbHelper myDbHelper = new MyDbHelper(this);
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        // DBのClose: 気にしない

        String [] from = {
            MyAppContract.Users.COLUMN_NAME,
            MyAppContract.Users.COLUMN_SCORE
        };
        int [] to = {
            android.R.id.text1,
            android.R.id.text2
        };
        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                null,
                from,
                to,
                0
        );

        // List
        ListView myListView = (ListView) findViewById(R.id.myListView);
        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // INSERT
//                ContentValues values = new ContentValues();
//                values.put(MyAppContract.Users.COLUMN_NAME, "tanaka");
//                values.put(MyAppContract.Users.COLUMN_SCORE, 50);
//                getContentResolver().insert(MyContentProvider.CONTENT_URI, values);

                // DELETE
//                Uri uri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, id);
//                String selection = MyAppContract.Users.COLUMN_ID + " = ?";
//                String [] selectionArgs = {Long.toString(id)};
//                getContentResolver().delete(uri, selection, selectionArgs);

                // UPDATE
                ContentValues values = new ContentValues();
                values.put(MyAppContract.Users.COLUMN_SCORE, 100);
                Uri uri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, id);
                String selection = MyAppContract.Users.COLUMN_ID + " = ?";
                String [] selectionArgs = {Long.toString(id)};
                getContentResolver().update(uri, values, selection, selectionArgs);
            }
        });


        // (1) Loaderの初期化
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // (2) 非同期処理を投げる
        String [] projection = {
                MyAppContract.Users.COLUMN_ID,
                MyAppContract.Users.COLUMN_NAME,
                MyAppContract.Users.COLUMN_SCORE
        };
        return new CursorLoader(
                this,
                MyContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // (3) 結果をadapterに反映
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // (3) 結果をadapterに反映
        adapter.swapCursor(null);

    }
}
