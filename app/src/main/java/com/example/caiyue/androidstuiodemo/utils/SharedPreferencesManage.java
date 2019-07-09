package com.example.caiyue.androidstuiodemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.caiyue.androidstuiodemo.MainApplication;

import java.util.Map;

public class SharedPreferencesManage {
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    static  SharedPreferencesManage sharedPreferencesManage;
    public static SharedPreferencesManage getIntsance(Context context, String key){
        synchronized (SharedPreferencesManage.class) {
            if (sharedPreferencesManage == null) {
                sharedPreferencesManage = new SharedPreferencesManage(context, key);
            }
            return sharedPreferencesManage;
        }
    }
    public SharedPreferencesManage(Context context, String key) {
        sharedPreferences = context.getSharedPreferences(key, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public Boolean add(String key, Object value){
        if (value instanceof String) {
            editor.putString(key, (String)value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer)value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            return false;
        }
        editor.commit();
        return true;
    }
    public Object get(String key, Object type){
        Object result = null;
        if (type instanceof String) {
            result = sharedPreferences.getString(key, (String)type);
        } else if (type instanceof Integer) {
            result = sharedPreferences.getInt(key, (Integer)type);
        }  else if (type instanceof Boolean) {
            result = sharedPreferences.getBoolean(key, (Boolean) type);
        }
        return result;
    }
    public Boolean remove(String key){
        editor.remove(key);
        editor.commit();
        return true;
    }
    public Map<String, ?> getAll(){
        return sharedPreferences.getAll();
    }
}
