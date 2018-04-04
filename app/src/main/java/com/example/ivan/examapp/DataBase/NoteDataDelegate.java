package com.example.ivan.examapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class NoteDataDelegate {

    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;

    static final String TABLE_NOTES = "answers";

    public NoteDataDelegate(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

     public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_NOTES, name);
    }

    public String getAllNotes(String select) {
        //dbHelper.query("answers", null,  null, null, null, null, null);

        Cursor cursor = database.rawQuery(select, null);

        cursor.moveToFirst();
        String super_answer = cursor.getString(0);
        return super_answer;
    }

    public List<Integer> getValues(String select) {
        List<Integer> values = new ArrayList<>();
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int currentValue = Integer.parseInt(cursor.getString(cursor.getColumnIndex("test_id")));
                values.add(currentValue);
                cursor.moveToNext();
            }
        }
        return values;
    }

}
