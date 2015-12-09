package com.aspirecn.hop.sample2.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.aspirecn.hop.sample2.service.aidl.IMyAidlInterface;

/**
 * 自定义一Service，内部实现{@link IMyAidlInterface},对外提供IBinder
 * Created by yinghuihong on 15/12/9.
 */
public class ArithmeticService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int add(int num1, int num2) throws RemoteException {
            return num1 + num2;
        }
    };

}
