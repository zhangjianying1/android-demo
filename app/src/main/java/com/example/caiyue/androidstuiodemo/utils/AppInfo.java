package com.example.caiyue.androidstuiodemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.caiyue.androidstuiodemo.MainApplication;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class AppInfo {
    private static JsonObject appInfo;
    private static Map<String,Object> info = new HashMap<>();

    @SuppressLint("MissingPermission")
    public static Map<String,Object> getAppInfo(Context context){
        if (info.isEmpty()) {
            String apkName = getAppName(context);
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifi.getConnectionInfo();
            StringBuilder deviceId = new StringBuilder();
            try {
                info.put("appId", "joyshebao");
                info.put("versionName", context.getPackageManager().getPackageInfo(apkName,0).versionName);
                info.put("versionCode", context.getPackageManager().getPackageInfo(apkName,0).versionCode);
                info.put("DeviceSoftwareVersion", tm.getDeviceSoftwareVersion());
                info.put("Line1Number", tm.getLine1Number());
                info.put("NetworkCountryIso", tm.getNetworkCountryIso());
                info.put("NetworkOperator", tm.getNetworkOperator());
                info.put("NetworkOperatorName", tm.getNetworkOperatorName());
                info.put("wifiMac", wifiInfo.getMacAddress());
                info.put("NetworkType", tm.getNetworkType());
                info.put("PhoneType", tm.getPhoneType());
                info.put("SimCountryIso", tm.getSimCountryIso());
                info.put("SimOperator", tm.getSimOperator());
                info.put("SimOperatorName", tm.getSimOperatorName());
                info.put("SimSerialNumber", tm.getSimSerialNumber());
                info.put("SimState", tm.getSimState());
                info.put("SubscriberId(IMSI)", tm.getSubscriberId());
                info.put("VoiceMailNumber", tm.getVoiceMailNumber());
                info.put("osLanguage", Locale.getDefault().getLanguage());
                info.put("os", android.os.Build.VERSION.RELEASE);
                info.put("model", android.os.Build.MODEL);
                info.put("brand", android.os.Build.BRAND);
                String imei = tm.getDeviceId();
                if(!isEmpty(imei)){
                    deviceId.append("imei");
                    deviceId.append(imei);
                }
                //序列号（sn）
                String sn = tm.getSimSerialNumber();
                if(!isEmpty(sn)){
                    deviceId.append("sn");
                    deviceId.append(sn);
                }
                info.put("deviceId", deviceId.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("request", info.toString());
        return info;
    }
    public static String getAppName(Context context) {
        String appName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            appName = applicationInfo.processName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }
}
