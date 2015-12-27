package com.arugan.immunity.immunity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by nagura on 2015/10/20.
 */
public class Notifier extends BroadcastReceiver {

    @Override
    public void onReceive(Context content, Intent intent) {
        //通知がクリックされた時に発行されるIntentの生成
        Intent sendIntent = new Intent(content, MainActivity.class);
        PendingIntent sender = PendingIntent.getActivity(content, 0, sendIntent, 0);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //通知オブジェクトの生成
        Notification noti = new NotificationCompat.Builder(content)
                .setTicker("舌下免疫の時間です")
                .setContentTitle("【通知】舌下免疫の時間です")
                .setContentText("舌下免疫を設定した時間がきました")
                .setSmallIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha)
                .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                .setSound(uri)
                .setAutoCancel(true)
                .setContentIntent(sender)
                .build();

        NotificationManager manager = (NotificationManager)content.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, noti);

//        SetTimeActivity setTimeActivity = new SetTimeActivity();
//        setTimeActivity.setNextNotifier();
    }
}