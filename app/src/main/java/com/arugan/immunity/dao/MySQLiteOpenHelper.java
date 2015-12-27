package com.arugan.immunity.dao;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arugan.immunity.immunity.Notifier;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by nagura on 2015/12/08.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    static final String DB = "sqlite_sample.db";
    static final int DB_VERSION = 1;
    static final String CREATE_TABLE = "create table immunity ( _id integer primary key autoincrement, reg_date text);";
    static final String DROP_TABLE = "drop table immunity;";

    static final String CREATE_NOTIFIER_TIME_TABLE = "create table notifer_time ( _id integer primary key autoincrement,  hour integer, minute integer);";

    static final String CREATE_REMAIN_COUNT_TABLE = "create table remain_count ( _id integer primary key autoincrement,  remain integer);";

//    private SQLiteDatabase mydb;

    public MySQLiteOpenHelper(Context c) {
        super(c, DB, null, DB_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTIFIER_TIME_TABLE);
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_REMAIN_COUNT_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

}