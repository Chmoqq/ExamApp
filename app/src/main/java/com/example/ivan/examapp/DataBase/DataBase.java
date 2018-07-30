package com.example.ivan.examapp.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ivan.examapp.MainActivity;
import com.example.ivan.examapp.Ticket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DataBase {

    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;

    public DataBase(Context context) {
        dbHelper = new DataBaseHelper(context);
        try {
            copyDB(context);
        } catch (IOException e) {
            System.out.println("ЕБАААААТЬ!!!!!!!!!!!!!!");
        }
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

        cursor.close();
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

    public String getAnswersCount() {
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

    public float getCompletedAnswers(int test_id) {
        String query = "select count(*) as total_answers from user_answers where test_id = " + test_id;
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();

        int total_answers = cursor.getInt(cursor.getColumnIndex("total_answers"));
        if (total_answers == 0)
            return 0;

        query = "select count(*) as valid_answers from user_answers join answers on answers.question_id = user_answers.question_id and answers.test_id = user_answers.test_id where user_answers.test_id = " + test_id + " and user_answers.answer_1 = answers.answer_1 and user_answers.answer_2 = answers.answer_2 and user_answers.answer_3 = answers.answer_3 and user_answers.answer_4 = answers.answer_4";
        cursor = database.rawQuery(query, null);

        cursor.moveToFirst();

        int valid_answers = cursor.getInt(cursor.getColumnIndex("valid_answers"));
        return valid_answers / total_answers;
    }

    private void copyDB(Context context) throws IOException {

        String destPath = "/data/data/" + context.getPackageName()
                + "/databases/answers";

        File f = new File(destPath);
        if (!f.exists()) {
            OutputStream out = new FileOutputStream(f);
            InputStream in = context.getAssets().open("answers");
            int size = in.available();
            byte[] buffer = new byte[size];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            in.close();
            out.close();
        }
    }

    public List<List<String>> getAnswers(int test_id) {
        List<List<String>> values1 = new ArrayList<>();
        Cursor cursor;

        dbHelper.onOpen(database);
        String query = "SELECT answer_1, answer_2, answer_3, answer_4 FROM answers WHERE test_id=" + test_id;
        cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                List<String> values = new ArrayList<>();

                for (int i = 1; i <= 4; i++) {
                    int column_index = cursor.getColumnIndex(String.format("answer_%s", i));

                    values.add(cursor.isNull(column_index) ? null : cursor.getString(column_index));
                }
                values1.add(values);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return values1;
    }

    public void userAnswerInsert(int test_id, int question_id, List<String> userAns) {
        long time = System.currentTimeMillis();
        database.execSQL(
                "INSERT INTO user_answers (answer_id, test_id, question_id, answer_1, answer_2, answer_3, answer_4, date_finished) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{test_id, question_id, userAns.get(0), userAns.get(1), userAns.get(2), userAns.get(3), time}
        );
    }

    public int[] getStats(int subject_id, int type) {
        Cursor cursor;

        List<Long> points = Arrays.asList(864000000L, 1728000000L, 2592000000L);

        String right_answers_query;
        String total_answers_query;

        switch (type) {
            case 1:
                total_answers_query = "select count(*) as total_answers from user_answers join answers on answers.question_id = user_answers.question_id and answers.test_id = user_answers.test_id" +
                        " where user_answers.test_id in (select id from tests where subject_id = " + subject_id + ") and user_answers.date_finished > " + (System.currentTimeMillis() - points.get(2)) + " and" +
                        " user_answers.answer_1 is not answers.answer_1 and user_answers.answer_2 is not answers.answer_2" +
                        " and user_answers.answer_3 is not answers.answer_3 and user_answers.answer_4 is not answers.answer_4";
                right_answers_query = "select count(*) as valid_answers from user_answers join answers on answers.question_id = user_answers.question_id and answers.test_id = user_answers.test_id" +
                        " where user_answers.test_id in (select id from tests where subject_id = " + subject_id + ") and user_answers.date_finished > " + (System.currentTimeMillis() - points.get(2)) + " and" +
                        " user_answers.answer_1 is answers.answer_1 and user_answers.answer_2 is answers.answer_2" +
                        " and user_answers.answer_3 is answers.answer_3 and user_answers.answer_4 is answers.answer_4";
                cursor = database.rawQuery(total_answers_query, null);
                cursor.moveToFirst();
                int total = cursor.getInt(cursor.getColumnIndex("total_answers"));

                cursor = database.rawQuery(right_answers_query, null);
                cursor.moveToFirst();
                int valid = cursor.getInt(cursor.getColumnIndex("valid_answers"));

                return new int[]{total, valid};
            case 2:
                String query = "select count(*) as total_answers from user_answers join answers on answers.question_id = user_answers.question_id and answers.test_id = user_answers.test_id" +
                        " where user_answers.test_id in (select id from tests where subject_id = " + subject_id + ") and user_answers.date_finished > " + (System.currentTimeMillis() - points.get(2)) + " and user_answers.date_finished < " + (System.currentTimeMillis() - points.get(1)) +
                        " and" +
                        " user_answers.answer_1 is not answers.answer_1 and user_answers.answer_2 is not answers.answer_2" +
                        " and user_answers.answer_3 is not answers.answer_3 and user_answers.answer_4 is not answers.answer_4";
                cursor = database.rawQuery(query, null);
                cursor.moveToFirst();
                int first = cursor.getInt(cursor.getColumnIndex("total_answers"));

                query = "select count(*) as total_answers from user_answers join answers on answers.question_id = user_answers.question_id and answers.test_id = user_answers.test_id" +
                        " where user_answers.test_id in (select id from tests where subject_id = " + subject_id + ") and user_answers.date_finished > " + (System.currentTimeMillis() - points.get(1)) + " and user_answers.date_finished < " + (System.currentTimeMillis() - points.get(0)) +
                        " and" +
                        " user_answers.answer_1 is not answers.answer_1 and user_answers.answer_2 is not answers.answer_2" +
                        " and user_answers.answer_3 is not answers.answer_3 and user_answers.answer_4 is not answers.answer_4";
                cursor = database.rawQuery(query, null);
                cursor.moveToFirst();
                int second = cursor.getInt(cursor.getColumnIndex("total_answers"));

                query = "select count(*) as total_answers from user_answers join answers on answers.question_id = user_answers.question_id and answers.test_id = user_answers.test_id" +
                        " where user_answers.test_id in (select id from tests where subject_id = " + subject_id + ") and user_answers.date_finished > " + (System.currentTimeMillis() - points.get(0)) + " and user_answers.date_finished < " + System.currentTimeMillis() +
                        " and " +
                        " user_answers.answer_1 is not answers.answer_1 and user_answers.answer_2 is not answers.answer_2" +
                        " and user_answers.answer_3 is not answers.answer_3 and user_answers.answer_4 is not answers.answer_4";
                cursor = database.rawQuery(query, null);
                cursor.moveToFirst();
                int third = cursor.getInt(cursor.getColumnIndex("total_answers"));
                return new int[]{first, second, third};
        }
        return null;
    }

}
