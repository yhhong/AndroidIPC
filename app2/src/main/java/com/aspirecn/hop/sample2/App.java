package com.aspirecn.hop.sample2;

import android.app.Application;
import android.util.Log;

/**
 * Created by yinghuihong on 15/12/1.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("App2", "Application onCreate() 指定在新的任务栈执行");
    }
}
