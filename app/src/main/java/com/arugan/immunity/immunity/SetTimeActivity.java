package com.arugan.immunity.immunity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TimePicker;
import android.widget.Toast;

import com.arugan.immunity.DailyScheduler;
import com.arugan.immunity.dao.MySQLiteOpenHelper;

import java.util.Calendar;
import java.util.TimeZone;

public class SetTimeActivity extends ActionBarActivity {

    TimePickerDialog timePickerDialog;
//    static SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar calendar = Calendar.getInstance(tz);

        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
                    SQLiteDatabase mydb = hlpr.getWritableDatabase();
                    DailyScheduler dailyScheduler = new DailyScheduler(getApplicationContext(), mydb);
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minuite) {
                        dailyScheduler.setNotifierTime(Notifier.class, hour, minuite);
                        finish();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

}
