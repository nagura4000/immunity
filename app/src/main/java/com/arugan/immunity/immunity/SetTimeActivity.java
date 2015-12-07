package com.arugan.immunity.immunity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class SetTimeActivity extends ActionBarActivity {

    TimePickerDialog timePickerDialog;
//    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//        @Override
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            // 設定した時間をトーストで表示
//            Toast.makeText(
//                    SetTimeActivity.this,
//                    "time set to " + Integer.toString(hourOfDay) + " : "
//                            + Integer.toString(minute), Toast.LENGTH_LONG)
//                    .show();
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar calendar = Calendar.getInstance(tz);
//        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        final int minute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minuite) {

                        // 日本(+9)以外のタイムゾーンを使う時はここを変える


                        //今日の目標時刻のカレンダーインスタンス作成
                        Calendar cal_target = Calendar.getInstance();
                        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
                        cal_target.setTimeZone(tz);
                        cal_target.set(Calendar.HOUR_OF_DAY, hour);
                        cal_target.set(Calendar.MINUTE, minuite);
                        cal_target.set(Calendar.SECOND, 0);

//                        //呼び出す日時を設定する
//                        Calendar triggerTime = Calendar.getInstance();
//                        cal_target.add(Calendar.SECOND, 5);	//今から5秒後

//                        System.out.println(triggerTime.getTimeInMillis());
//                        //設定した日時で発行するIntentを生成
//                        Intent intent = new Intent(SetTimeActivity.this, Notifier.class);
//                        PendingIntent sender = PendingIntent.getBroadcast(SetTimeActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                        //日時と発行するIntentをAlarmManagerにセットします
//                        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
//                        manager.set(AlarmManager.RTC_WAKEUP, triggerTime.getTimeInMillis(), sender);

                        //設定した日時で発行するIntentを生成
                        Intent intent = new Intent(SetTimeActivity.this, Notifier.class);
                        PendingIntent sender = PendingIntent.getBroadcast(SetTimeActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                        manager.set(AlarmManager.RTC_WAKEUP, cal_target.getTimeInMillis(), sender);

                        finish();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
}
