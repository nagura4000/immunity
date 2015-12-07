package com.arugan.immunity.immunity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    static final String DB = "sqlite_sample.db";
    static final int DB_VERSION = 1;
    static final String CREATE_TABLE = "create table immunity ( _id integer primary key autoincrement, reg_date text);";
    static final String DROP_TABLE = "drop table immunity;";
    private static final int  BORDER_WEIGHT = 2;
    static SQLiteDatabase mydb;

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper(Context c) {
            super(c, DB, null, DB_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();

        Cursor cursor = mydb.query("immunity", new String[]{"_id", "reg_date"}, null, null, null, null, "_id DESC");
        TableLayout tableLayout = (TableLayout) findViewById(R.id.vewTable);
        while (cursor.moveToNext()){
            TextView textView = createTextView(cursor.getString(cursor.getColumnIndex("reg_date")));
            tableLayout.addView(textView);
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(15);
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
            case R.id.action_reset:
                mydb.execSQL("delete from immunity;");
                TableLayout tableLayout = (TableLayout) findViewById(R.id.vewTable);
                tableLayout.removeAllViews();
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

                TextView textView = createTextView(regDate);
                tableLayout.addView(textView, 0);

//                Cursor cursor = mydb.query("immunity", new String[]{"_id", "reg_date"}, null, null, null, null, "_id DESC");
//                while (cursor.moveToNext()){
//                    TextView textView = new TextView(this);
//                    textView.setText(cursor.getString(cursor.getColumnIndex("reg_date")));
//                    tableLayout.addView(textView);
//                }


//                ((TextView)tableRow1.getChildAt(0)).setText("テスト");
//                tableRow1.addView(createButton("セル１－１"));
        }
    }

    private Button createButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        return button;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.button:
//                TableLayout tableLayout = (TableLayout) findViewById(R.id.vewTable);
//
//                TableRow tableRow1 = new TableRow(this);
//                tableLayout.addView(tableRow1);
//                ((TextView)tableRow1.getChildAt(0)).setText("テスト");
//        }
//    }
}
