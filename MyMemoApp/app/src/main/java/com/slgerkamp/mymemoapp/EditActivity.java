package com.slgerkamp.mymemoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;


public class EditActivity extends Activity {

    private boolean isNewMemo = true;
    private long memoId;

    private EditText myMemoTitle;
    private EditText myMemoBody;
    private TextView myMemoUpdated;

    private String title = "";
    private String body = "";
    private String updated = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        myMemoTitle = (EditText) findViewById(R.id.myMemoTitle);
        myMemoBody = (EditText) findViewById(R.id.myMemoBody);
        myMemoUpdated = (TextView) findViewById(R.id.myMemoUpdated);

        memoId = getIntent().getLongExtra(MainActivity.EXTRA_MY_ID, 0L);
        isNewMemo = (memoId == 0L);

        if (isNewMemo) {
            getActionBar().setTitle("New Memo");
        } else {
            getActionBar().setTitle("Edit Memo");
            Uri uri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, memoId);
            String [] projection = {
                    MyMemoContract.Memos.COLUMN_TITLE,
                    MyMemoContract.Memos.COLUMN_BODY,
                    MyMemoContract.Memos.COLUMN_UPDATED,
            };
            String selection = MyMemoContract.Memos.COLUMN_ID + " = ? ";
            String [] selectionArgs = {Long.toString(memoId)};
            Cursor cursor = getContentResolver().query(
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    null
            );

            while (cursor.moveToNext()){
                title = cursor.getString(cursor.getColumnIndex(MyMemoContract.Memos.COLUMN_TITLE));
                body = cursor.getString(cursor.getColumnIndex(MyMemoContract.Memos.COLUMN_BODY));
                updated = "Updated : " + cursor.getString(cursor.getColumnIndex(MyMemoContract.Memos.COLUMN_UPDATED));
            }
            myMemoTitle.setText(title);
            myMemoBody.setText(body);
            myMemoUpdated.setText(updated);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);

        if(isNewMemo){
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_save:

                // 入力値の取得（ここ自動化できる）
                title = myMemoTitle.getText().toString().trim();
                body = myMemoBody.getText().toString().trim();

                // 入力値のバリデーション（ここ自動化できる）
                if(title.isEmpty()){
                    Toast.makeText(
                            this,
                            "please enter title",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(MyMemoContract.Memos.COLUMN_TITLE, title);
                    values.put(MyMemoContract.Memos.COLUMN_BODY, body);
                    if(isNewMemo){
                        // 新規登録
                        getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
                    } else {
                        // 更新

                        values.put(
                                MyMemoContract.Memos.COLUMN_UPDATED,
                                android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString()
                        );
                        Uri uri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, memoId);
                        String selection = MyMemoContract.Memos.COLUMN_ID + " = ? ";
                        String [] selectionArgs = {Long.toString(memoId)};
                        getContentResolver().update(
                                uri,
                                values,
                                selection,
                                selectionArgs
                        );
                    }
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;
            case R.id.action_delete:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Delete Memo");
                alertDialog.setMessage("Are you sure to delete this memo?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = ContentUris.withAppendedId(MyContentProvider.CONTENT_URI, memoId);
                        String selection = MyMemoContract.Memos.COLUMN_ID + " = ? ";
                        String[] selectionArgs = {Long.toString(memoId)};
                        getContentResolver().delete(uri, selection, selectionArgs);

                        Intent intent = new Intent(EditActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                alertDialog.create().show();

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
