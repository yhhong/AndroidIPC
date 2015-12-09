package com.aspirecn.hop.sample2;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static String INTENT_ACTION_NOTIFICATION = "com.aspirecn.hop.notification";

    protected MyReceiver mReceiver = new MyReceiver();

    @Bind(R.id.tv_show)
    TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ButterKnife.bind(this);

        tvShow.setText(String.format("%s is the current packageName the same as applicationId.", getPackageName()));

        if (mReceiver == null) {
            mReceiver = new MyReceiver();
        }
        registerReceiver(mReceiver, new IntentFilter(INTENT_ACTION_NOTIFICATION));
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
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public class MyReceiver extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(MainActivity.this, "Broadcast receiver", Toast.LENGTH_LONG).show();

            if (intent != null) {
                Bundle extras = intent.getExtras();
                String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
                int notificationIcon = extras.getInt(Notification.EXTRA_SMALL_ICON);
                Bitmap notificationLargeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON);
                CharSequence notificationText = extras.getCharSequence(Notification.EXTRA_TEXT);
                CharSequence notificationSubText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

                tvShow.setText(String.format("title:%s,\ntext:%s,\nsubText:%s", notificationTitle, notificationText, notificationSubText));
                Drawable largeIcon = null;
                if (notificationLargeIcon != null) {
                    largeIcon = new BitmapDrawable(getResources(), notificationLargeIcon);
                    largeIcon.setBounds(0, 0, largeIcon.getMinimumWidth(), largeIcon.getMinimumHeight());

                }

                Drawable smallIcon = null;
//                if (notificationIcon != 0) {
//                    smallIcon = getResources().getDrawable(notificationIcon);
//                    assert smallIcon != null;
//                    smallIcon.setBounds(0, 0, smallIcon.getMinimumWidth(), smallIcon.getMinimumHeight());
//                }
                tvShow.setCompoundDrawables(null, largeIcon, null, smallIcon);
            }

        }
    }
}
