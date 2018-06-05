package com.example.ivan.examapp.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ivan.examapp.MainActivity;
import com.example.ivan.examapp.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class DataBase {

    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;

    public DataBase(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
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
        cursor.close();
        return values;
    }

    public String getSubjects() {
        String query = "SELECT id FROM tests WHERE subject_id=" + MainActivity.getCurSubjectId();
        List<String> values = new ArrayList<>();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String subjectName = cursor.getString(cursor.getColumnIndex("id"));
                values.add(subjectName);
                cursor.moveToNext();
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            stringBuilder.append("'" + values.get(i) + "'" + ", ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        cursor.close();
        return "SELECT COUNT(question_id) FROM answers WHERE test_id in (" + stringBuilder.toString() + ")";
    }

    public List<Integer> getAnswers(int test_id, int list_length) {
        List<Integer> values = new ArrayList<>();
        String query = "SELECT answer_1, answer_2, answer_3, answer_4 FROM answers WHERE test_id=" + test_id + " AND question_id=" + list_length;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                for (int i = 1; i <= 4; i++) {
                    int column_index = cursor.getColumnIndex(String.format("answer_%s", i));

                    values.add(cursor.isNull(column_index) ? null : cursor.getInt(column_index));
                }

                cursor.moveToNext();
            }
        }
        cursor.close();
        return values;
    }

    public void userAnswerInsert(int test_id, int question_id, List<Integer> userAns) {
        database.execSQL(
                "INSERT INTO user_answers (answer_id, test_id, question_id, answer_1, answer_2, answer_3, answer_4) VALUES (NULL, ?, ?, ?, ?, ?, ?)",
                new Object[]{test_id, question_id, userAns.get(0), userAns.get(1), userAns.get(2), userAns.get(3)}
        );
    }

}
