package com.arugan.immunity.immunity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.arugan.immunity.dao.MySQLiteOpenHelper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemainCounterActivity extends ActionBarActivity {

    private SQLiteDatabase mydb;
    private EditText remainText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remain_counter);

        remainText = (EditText) findViewById(R.id.remainCountText);

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();

        Cursor cursor = mydb.query("remain_count", new String[]{"_id", "remain"}, null, null, null, null, "_id DESC");
        if(!cursor.moveToFirst()) {
            remainText.setText("0");
            ContentValues values = new ContentValues();
            values.put("_id", 0);
            values.put("remain", 0);
            mydb.insert("remain_count", null, values);
        } else {
            int remainCount = cursor.getInt(cursor.getColumnIndex("remain"));
            remainText.setText(String.valueOf(remainCount));
        }

//        // アイコン表示を有効にする
//        requestWindowFeature(Window.FEATURE_LEFT_ICON);
//        setContentView(R.layout.activity_remain_counter);
//        // アイコン表示
//        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.abc_ic_clear_mtrl_alpha);

//        final Window w = getWindow();
//        w.requestFeature(Window.FEATURE_LEFT_ICON);
//        setContentView(R.layout.activity_remain_counter);
//        w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_zekka);

//        getActionBar().setDisplayShowHomeEnabled(true);
//        getActionBar().setDisplayShowCustomEnabled(true);
//        getActionBar().setLogo(R.drawable.ic_zekka); // ホーム画面のロゴ
//        getActionBar().setIcon(R.drawable.ic_zekka); // ホーム以外で表示するアイコン
//        getActionBar().setDisplayUseLogoEnabled(false); // ロゴを非表示
    }

    public void onClickRegister(View view) {
        switch (view.getId()) {
            case R.id.remainRegister:

                String text = remainText.getText().toString();

                int remainCount = -1;
                try {
                    remainCount = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    remainCount = 0;
                }

                ContentValues values = new ContentValues();
                values.put("_id", 0);
                values.put("remain", remainCount);
                mydb.update("remain_count", values, null, null);

                new AlertDialog.Builder(this)
                        .setTitle("薬の残り数")
                        .setMessage("更新完了です")
                        .setPositiveButton("OK", null)
                        .show();
        }
    }
}
