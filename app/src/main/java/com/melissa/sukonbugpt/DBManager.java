package com.melissa.sukonbugpt;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.COLUMN_NAME, name);
        return database.insert(DBHelper.TABLE_NAME, null, contentValue);
    }

    public ArrayList<String> getAllData() {
        ArrayList<String> data = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
                data.add(name);
            }
            cursor.close();
        }
        return data;
    }

    // 모든 데이터 삭제
    public void deleteAllData() {
        database.delete(DBHelper.TABLE_NAME, null, null);
    }

    // 특정 번호에 해당하는 데이터 삭제
    public void deleteDataById(long id) {
        String selection = DBHelper.COLUMN_ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
    }

    // 기본키 재정렬
    public void reorderIds() {
        // 현재 데이터베이스 버전
        int currentVersion = database.getVersion();

        // 데이터베이스를 업그레이드하여 기본키 재정렬
        dbHelper.onUpgrade(database, currentVersion, currentVersion + 1);
    }
}