package com.arugan.immunity.immunity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.arugan.immunity.DailyScheduler;
import com.arugan.immunity.dao.MySQLiteOpenHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Typeface.BOLD;


public class MainActivity extends ActionBarActivity {

    static SQLiteDatabase mydb;

    private ProgressBar progressBar1;
    private Timer timer;
    private ProggressTimerTask proggressTimerTask;
    private Handler handler = new Handler();
    private int count;

    private TextView remainCountMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_zekka);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setTitle(R.string.app_name);

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();
//        hlpr.onCreate(mydb);

        int remainCount = 0;
        Cursor remainCursor = mydb.query("remain_count", new String[]{"_id", "remain"}, null, null, null, null, "_id DESC");
        if(remainCursor.moveToFirst()) {
            remainCount = remainCursor.getInt(remainCursor.getColumnIndex("remain"));
        }

        remainCountMsg = (TextView)findViewById(R.id.remainCount);
        remainCountMsg.setText("薬の残数：" + remainCount);

        Cursor cursor = mydb.query("immunity", new String[]{"_id", "reg_date"}, null, null, null, null, "_id DESC");
        TableLayout tableLayout = (TableLayout) findViewById(R.id.vewTable);
        while (cursor.moveToNext()){
            TextView textView = createTextView(cursor.getString(cursor.getColumnIndex("reg_date")));
            tableLayout.addView(textView);
        }

        progressBar1 = (ProgressBar)findViewById(R.id.progressBar);
//        progressBar1.setMax(100); // 水平プログレスバーの最大値を設定
//        progressBar1.setProgress(20); // 水平プログレスバーの値を設定
//        progressBar1.setSecondaryProgress(60); // 水平プログレスバーのセカンダリ値を設定
//        requestWindowFeature(Window.FEATURE_LEFT_ICON);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    private TextView createTextView(String text, int... size) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        if (size.length > 0) {
            textView.setTextSize(size[0]);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            textView.setTextSize(18);
        }
        textView.setBackgroundColor(Color.WHITE);
        Drawable dw = getResources().getDrawable(R.drawable.text_view);
        textView.setBackground(dw);
        return textView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    public void onClicMenu(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SetTimeActivity.class);
                startActivity(intent);
                break;
            case R.id.action_remain:
                Intent remain = new Intent(this, RemainCounterActivity.class);
                startActivity(remain);
                break;
            case R.id.action_reset:
//                mydb.execSQL("delete from immunity;");
//                mydb.execSQL("delete from notifer_time;");
//                TableLayout tableLayout = (TableLayout) findViewById(R.id.vewTable);
//                tableLayout.removeAllViews();
//                DailyScheduler dailyScheduler = new DailyScheduler(getApplicationContext(), mydb);
//                dailyScheduler.cancelAlarm();

                new AlertDialog.Builder(this)
                        .setTitle("初期化")
                        .setMessage("登録したデータが全て消去されますが、よいですか？")
                        .setPositiveButton("初期化実行", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // OK button pressed
                                mydb.execSQL("delete from immunity;");
                                mydb.execSQL("delete from notifer_time;");
                                mydb.execSQL("delete from remain_count;");
                                TableLayout tableLayout = (TableLayout) findViewById(R.id.vewTable);
                                tableLayout.removeAllViews();
                                DailyScheduler dailyScheduler = new DailyScheduler(getApplicationContext(), mydb);
                                dailyScheduler.cancelAlarm();
                            }
                        })
                        .setNegativeButton("キャンセル", null)
                        .show();
                break;
        }
    }

    public void onClickRegister(View view) {
        switch (view.getId()) {
            case R.id.button:
                TableLayout tableLayout = (TableLayout) findViewById(R.id.vewTable);

                TableRow tableRow1 = new TableRow(this);
                tableLayout.addView(tableRow1);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String regDate = sdf.format(new Date());

                ContentValues values = new ContentValues();
                values.put("reg_date", regDate);
                mydb.insert("immunity", null, values);

                TextView textView = createTextView(regDate, 30);

                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_zekka, 0, 0, 0);

                tableLayout.addView(textView, 0);

                // ２分の飲み込み開始タイマー
                startTimer();
                // 薬の残数計算
                reduceRemainCount();
        }
    }

    private void reduceRemainCount() {
        int remainCount = 0;
        Cursor remainCursor = mydb.query("remain_count", new String[]{"_id", "remain"}, null, null, null, null, "_id DESC");
        if(remainCursor.moveToFirst()) {
            remainCount = remainCursor.getInt(remainCursor.getColumnIndex("remain"));
            remainCount--;
        }

        ContentValues values = new ContentValues();
        values.put("_id", 0);
        values.put("remain", remainCount);
        mydb.update("remain_count", values, null, null);

        remainCountMsg.setText("薬の残数：" + remainCount);
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        proggressTimerTask = new ProggressTimerTask();

        timer.schedule(proggressTimerTask, 0,1000);
        count = 0;
        progressBar1.setMax(120);
        progressBar1.setProgress(count);

    }

    private Button createButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        return button;
    }

    class ProggressTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    count++;
                    progressBar1.setProgress(count);

                    if (count >= progressBar1.getMax()) {
                        timer.cancel();
                        timer = null;
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int remainCount = 0;
        Cursor remainCursor = mydb.query("remain_count", new String[]{"_id", "remain"}, null, null, null, null, "_id DESC");
        if(remainCursor.moveToFirst()) {
            remainCount = remainCursor.getInt(remainCursor.getColumnIndex("remain"));
        }
        remainCountMsg.setText("薬の残数：" + remainCount);
    }
}

