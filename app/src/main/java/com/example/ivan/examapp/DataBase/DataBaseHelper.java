package com.example.ivan.examapp.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "answers";
    private String DATABASE_PATH = null;
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase myDataBase;


    DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.getReadableDatabase().query(table, null, null, null, null, null, null);
    }

}
