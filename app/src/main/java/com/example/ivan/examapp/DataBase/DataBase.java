package com.example.ivan.examapp.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ivan.examapp.Ticket;

import java.util.ArrayList;
import java.util.List;


public class DataBase {

    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;

    public DataBase(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public String getNote(String select) {
        String super_answer;
        Cursor cursor = database.rawQuery(select, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            super_answer = cursor.getString(0);
        } else {
            super_answer = "0";
        }

        return super_answer;
    }

    public List<Ticket> getTicket(String select) {
        List<Ticket> values = new ArrayList<>();
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String subjectName = cursor.getString(cursor.getColumnIndex("name"));
                int currentValue = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                Ticket currentTicket = new Ticket(subjectName, currentValue);
                values.add(currentTicket);
                cursor.moveToNext();
            }
        }
        return values;
    }

    public List<String> getList(String select) {
        List<String> values = new ArrayList<>();
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String subjectName = cursor.getString(cursor.getColumnIndex("id"));
                values.add(subjectName);
                cursor.moveToNext();
            }
        }
        return values;
    }

}
