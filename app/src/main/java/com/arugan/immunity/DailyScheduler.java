package com.arugan.immunity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arugan.immunity.immunity.Notifier;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by nagura on 2015/12/09.
 */
public class DailyScheduler {

    private Context context;
    private SQLiteDatabase mydb;

    public DailyScheduler(Context context, SQLiteDatabase mydb) {
        this.context = context;
        this.mydb = mydb;
    }

    public void cancelAlarm() {
        Intent intent = new Intent(context, Notifier.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);
        // アラームを解除する
        AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        am.cancel(pending);
    }

    private <T>void setAlarm(Class<T> launch_service, long targetTime) {

        Intent intent = new Intent(context, Notifier.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

//        long currentTimeMillis = System.currentTimeMillis();
//        targetTime =+ 32400000;
//        manager.set(AlarmManager.RTC_WAKEUP, currentTimeMillis + 5000, sender);
        //manager.setRepeating(AlarmManager.RTC, targetTime + 5000, AlarmManager.INTERVAL_DAY , sender);
        manager.setRepeating(AlarmManager.RTC, targetTime, AlarmManager.INTERVAL_DAY , sender);

//        Intent intent = new Intent(context, launch_service);
//
//        PendingIntent action = PendingIntent.getService(context, -1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
//
//        long currentTimeMillis = System.currentTimeMillis();
//        alarm.set(AlarmManager.RTC_WAKEUP, currentTimeMillis + 5000, action);
////        alarm.setRepeating(AlarmManager.RTC, targetTime, AlarmManager.INTERVAL_DAY, action);
    }

//    public void setNextNotifier() {
//        Cursor cursor = mydb.query("notifer_time", new String[]{"_id", "hour", "minute"}, null, null, null, null, "_id DESC");
//        cursor.moveToFirst();
//        int hour = cursor.getInt(cursor.getColumnIndex("hour"));
//        int minute = cursor.getInt(cursor.getColumnIndex("minute"));
//        setNotifierTime(hour, minute);
//    }

    public <T>void setNotifierTime(Class<T> launch_service, int hour, int minuite) {

        ContentValues values = new ContentValues();
        values.put("_id", 0);
        values.put("hour", hour);
        values.put("minute", minuite);

        Cursor cursor = mydb.query("notifer_time", new String[]{"_id", "hour", "minute"}, null, null, null, null, "_id DESC");
        if(!cursor.moveToFirst()) {
            mydb.insert("notifer_time", null, values);
        } else {
            mydb.update("notifer_time", values, null, null);
        }

        //今日の目標時刻のカレンダーインスタンス作成
        Calendar cal_target = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        cal_target.setTimeZone(tz);
        cal_target.set(Calendar.HOUR_OF_DAY, hour);
        cal_target.set(Calendar.MINUTE, minuite);
        cal_target.set(Calendar.SECOND, 0);

//        cal_target.setTimeZone(TimeZone.getTimeZone("UTC"));

        //現在時刻のカレンダーインスタンス作成
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTimeZone(tz);
//        long currentTimeMillis = System.currentTimeMillis();
        //ミリ秒取得
        long target_ms = cal_target.getTimeInMillis();
        long now_ms = cal_now.getTimeInMillis();

        //今日ならそのまま指定
        if (target_ms >= now_ms) {
            setAlarm(launch_service, target_ms);
            //過ぎていたら明日の同時刻を指定
        } else {
            cal_target.add(Calendar.DAY_OF_MONTH, 1);
            target_ms = cal_target.getTimeInMillis();
            setAlarm(launch_service, target_ms);
        }
    }


}
