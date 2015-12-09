package com.aspirecn.hop.sample1;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aspirecn.hop.sample1.notification.NewMessageNotification;
import com.aspirecn.hop.sample2.service.aidl.IMyAidlInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 应用间通讯（进程间通讯）
 * 一、Activity
 * 1、显示调用/隐式调用
 * 2、加入到当前任务/新任务的Activity栈
 * 3、传递数据
 * 4、回传数据
 * 5、可传递数据量限制大小
 * <p/>
 * 二、ContentProvider
 * ContentResolver
 * ContentObserver
 * Resource
 * 权限配置
 * 1、android:exported="true"
 * 2、permission针对各Uri进行权限配置
 * android:permission="string"
 * android:readPermission="string"
 * android:writePermission="string"
 * 3、android:exported="false" & android:shareUserId="com.aspirecn.hop"
 * <p/>
 * 三、Broadcast
 * <p/>
 * 四、Service AIDL
 * Server:
 * 1、定义IMyAidlInterface.aidl文件，build，生成对应的java文件在build/generated/source/aidl/debug/xxx.IMyAidlInterface.java
 * 2、创建Service文件，创建IBinder、实现IMyAidlInterface.java中的接口
 * 3、在manifest.xml中配置Service组件，action调用，并且指定在独立进程中运行，android:process=":remote"
 * Client:
 * 1、从Server拷贝aidl文件连同所在目录也保持一致
 * 2、bindService，成功后得到IBinder(aidlInterface)的引用，即可调用Server提供的AIDL接口；另外注意：bind service，从android5.0开始需要显示调用
 * <p/>
 * 五、NotificationListenerService
 */
public class MainActivity extends AppCompatActivity {

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

        initBindRemoteService();
    }

    private void initBindRemoteService() {
        mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                aidlInterface = null;
                Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding - Service disconnected");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                aidlInterface = IMyAidlInterface.Stub.asInterface((IBinder) service);
                Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding is done - Service connected");
            }
        };
        if (aidlInterface == null) {
            Intent intent = new Intent();
            intent.setAction("com.remote.service.CALCULATOR");
            Intent eIntent = new Intent(IntentUtil.getExplicitIntent(this, intent));
            //binding to remote service
            bindService(eIntent, mServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    @OnClick(R.id.btn_start_activity_obvious)
    void startActivityObvious() {
        try {
            Intent intent = new Intent();
            intent.putExtra("name", "hop"); //  携带传递数据
            intent.putExtra("password", "123456");
            intent.setClassName("com.aspirecn.hop.sample3", "com.aspirecn.hop.sample2.activity.RegisterActivity");  //  显示调用，注意：packageName取applicationId
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_start_activity_implicit)
    void startActivityImplicit() {
        try {
            Intent intent = new Intent();
            intent.putExtra("name", "hop"); //  携带传递数据
            intent.putExtra("password", "123456");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //   指定在新的任务执行
            intent.setAction("com.aspirecn.hop.sample2_login"); //  隐式调用
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_send_broadcast)
    void sendBroadcast() {
        NewMessageNotification.notify(this, "example string", 3);
    }

    @OnClick(R.id.btn_obtain_content)
    void obtainContent() {
        ContentResolver resolver = getContentResolver();
//        String type = resolver.getType(Uri.parse("content://com.aspirecn.hop.sample2.provider.MyContentProvider/teacher/1"));
//        Uri uri = Uri.parse("content://com.aspirecn.hop.sample2.MyContentProvider/teacher/2");
        Uri uri = Uri.parse("content://com.aspirecn.hop.sample2.MyContentProvider/teachers");
        // 1、get type
//        String type = resolver.getType(uri);
//        Toast.makeText(this, "" + type, Toast.LENGTH_SHORT).show();


        // 3、query
        Cursor cursor = resolver.query(uri, null, null, null, null);
        assert cursor != null;
        int count = cursor.getCount();
        cursor.close();
        Toast.makeText(this, "" + count, Toast.LENGTH_SHORT).show();


        // 2、insert
        ContentValues insertValue = new ContentValues();
        insertValue.put("name", "teacher");
        resolver.insert(uri, insertValue);

    }

    private IMyAidlInterface aidlInterface;

    private ServiceConnection mServiceConnection;

    @OnClick(R.id.btn_aidl_service)
    void aidlService() {
        try {
            int result = aidlInterface.add(1, 2);
            Toast.makeText(this, "result:" + result, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

}
