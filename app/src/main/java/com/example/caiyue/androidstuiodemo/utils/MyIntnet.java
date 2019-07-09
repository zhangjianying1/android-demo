package com.example.caiyue.androidstuiodemo.utils;

import android.content.Intent;
import android.net.Uri;

public class MyIntnet {
    public Intent callTel(String tel){
        Uri uri = Uri.parse("tel:" + tel);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        return intent;
    }
    public Intent openSetting(){
        return new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
    }
}
