package com.aspirecn.hop.sample2.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.aspirecn.hop.sample2.MainActivity;

/**
 * Created by yinghuihong on 15/12/3.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SimpleKitkatNotificationListener extends NotificationListenerService {

    private final String ACTION = "com.aspirecn.hop.notificationlistenerservice";

    private ClearNotificationBroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        //android.os.Debug.waitForDebugger();
        broadcastReceiver = new ClearNotificationBroadcastReceiver();

        registerReceiver(broadcastReceiver, new IntentFilter(ACTION));
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification = sbn.getNotification();
        if (mNotification != null) {

            Intent i = new Intent("xxx");
            sendBroadcast(i);
            // send broadcast
            Intent intent = new Intent(MainActivity.INTENT_ACTION_NOTIFICATION);
            intent.putExtras(mNotification.extras);
            sendBroadcast(intent);

            Notification.Action[] mActions = mNotification.actions;
            if (mActions != null) {
                for (Notification.Action mAction : mActions) {
                    int icon = mAction.icon;
                    CharSequence actionTitle = mAction.title;
                    PendingIntent pendingIntent = mAction.actionIntent;
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    class ClearNotificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
