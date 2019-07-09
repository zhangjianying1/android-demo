package com.example.caiyue.androidstuiodemo.services;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.igexin.sdk.GTServiceManager;

public class PushService extends Service {
    public static String ACTION_KEEPSERVICE = "android.intent.action.MAIN";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("push", "create");
        GTServiceManager.getInstance().onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("push", "start");
        return GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return GTServiceManager.getInstance().onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GTServiceManager.getInstance().onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GTServiceManager.getInstance().onLowMemory();
    }


}
