package com.example.caiyue.androidstuiodemo;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.example.caiyue.androidstuiodemo.services.PushService;
import com.foox.magic.sdk.Magic;

public class MainApplication extends Application {
    /**
     * 全局的上下文
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Magic.init(this, PushService.class.getName(), PushService.ACTION_KEEPSERVICE, 60*1000L);
    }

    /**
     * 获取context
     * @return
     */
    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
