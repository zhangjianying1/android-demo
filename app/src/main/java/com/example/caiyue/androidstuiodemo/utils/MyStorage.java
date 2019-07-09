package com.example.caiyue.androidstuiodemo.utils;

import android.content.Context;
import android.util.Log;

import com.example.caiyue.androidstuiodemo.model.UserModel;

public class MyStorage {
    private static MyStorage myStorage;
    private Context context;
    public static synchronized MyStorage getInstance(Context context){
        if (myStorage == null) {
            myStorage = new MyStorage();
            context = context;
        }
        return myStorage;
    }
    public UserModel getUserData(){
        String str = MySqlite.getInstance(context).getData("userData");
        Log.i("data", str);
        if (str != null) {
            return JsonManager.stringToBean(str, UserModel.class);
        } else {
            return null;
        }
    }
}
