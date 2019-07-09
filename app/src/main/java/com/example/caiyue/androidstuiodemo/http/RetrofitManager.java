package com.example.caiyue.androidstuiodemo.http;

import android.content.Context;
import android.util.Log;

import com.example.caiyue.androidstuiodemo.MainApplication;
import com.example.caiyue.androidstuiodemo.model.UserModel;
import com.example.caiyue.androidstuiodemo.utils.AppInfo;
import com.example.caiyue.androidstuiodemo.utils.JsonManager;
import com.example.caiyue.androidstuiodemo.utils.MySqlite;
import com.example.caiyue.androidstuiodemo.utils.MyStorage;
import com.example.caiyue.androidstuiodemo.utils.SharedPreferencesManage;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private String URL = "http://192.168.1.80/";
    private static final String TAG = "request";
    static RetrofitManager retrofitManager;
    private  static Context context;
    private static Map<String, Object> appInfo;
    public RetrofitManager(Context context) {
        this.context = context;
        appInfo = AppInfo.getAppInfo(context);
    }

    public static RetrofitManager getInstance(Context context){
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager(context);
        }
        return retrofitManager;
    }

    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(genericClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Map<String, Object> appInfo = AppInfo.getAppInfo(context);
                        UserModel userData = MyStorage.getInstance(context).getUserData();
//                        String userStr = MySqlite.getInstance(context).getData("userData");
//                        UserModel userData = JsonManager.stringToBean(userStr, UserModel.class);
                        String token = "", userId = "";
                        if (userData != null) {
                            token = userData.getToken();
                            userId = userData.getUserId();
                        }
                        Request request = chain.request()
                                .newBuilder()
//                                .header("Content-Type", "application/json")
                                .addHeader("appId", "joyshebao")
                                .addHeader("resVersion", (String) appInfo.get("versionName"))
                                .addHeader("userId", userId)
                                .addHeader("deviceId", (String)appInfo.get("deviceId"))
                                .addHeader("token", token)
                                .build();
                        Log.i(TAG, request.toString());
                        return chain.proceed(request);
                    }

                })
                .build();
        return httpClient;
    }
    public static RequestBody getRequstBodyFromMap(Map<String, Object> oMap){
        return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), JsonManager.mapToJson(oMap).toString());
    };
}
