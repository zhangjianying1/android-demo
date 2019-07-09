package com.example.caiyue.androidstuiodemo.http;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.caiyue.androidstuiodemo.MainApplication;
import com.example.caiyue.androidstuiodemo.model.BaseResponse;
import com.example.caiyue.androidstuiodemo.model.PersonInfo;
import com.example.caiyue.androidstuiodemo.model.SecurityModel;
import com.example.caiyue.androidstuiodemo.model.UserModel;
import com.example.caiyue.androidstuiodemo.utils.JsonManager;
import com.example.caiyue.androidstuiodemo.utils.MySqlite;
import com.example.caiyue.androidstuiodemo.utils.SharedPreferencesManage;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GolablRequest{
    public interface RequestCallback{
        void success(String str);
        void failure(String str);
    }

    /**
     * 查询用户信息
     * @param context
     * @param requestCallback
     */
    public static void queryUser(final Context context, final RequestCallback requestCallback){
        Map<String, Object> requestParams = new HashMap<String, Object>();
        RequestBody body = RetrofitManager.getRequstBodyFromMap(requestParams);
        RetrofitManager.getInstance(context).getRetrofit().create(MyServices.class).queryUser(body).enqueue(new Callback<BaseResponse<UserModel>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserModel>> call, Response<BaseResponse<UserModel>> response) {
                BaseResponse.Error error = (BaseResponse.Error) response.body().getError();

                // 用户下线状态
                if (error != null) {
                    requestCallback.failure("用户未登录");
                    MySqlite.getInstance(context).delete("userData");
                } else {
                    String jsonString = JsonManager.beanToJSONString(response.body().getData());
                    MySqlite.getInstance(context).insertData("userData", jsonString);
                    requestCallback.success(jsonString);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserModel>> call, Throwable t) {

            }
        });
    }
    /**
     *  获取社保卡信息
     * @param context
     * @param securityId
     * @param requestCallback
     */
    public static void getSecurityDataById(final Context context, final String securityId, final Boolean isRefresh, final RequestCallback requestCallback){
        String secStr = MySqlite.getInstance(context).getData("security_" + securityId);
        if (!isRefresh && !secStr.isEmpty()) {
            requestCallback.success(secStr);
        } else {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("securityId", securityId);
            RequestBody body = RetrofitManager.getRequstBodyFromMap(param);

            RetrofitManager.getInstance(context).getRetrofit().create(MyServices.class).getSecInfo(body).enqueue(new Callback<BaseResponse<SecurityModel>>() {
                @Override
                public void onResponse(Call<BaseResponse<SecurityModel>> call, Response<BaseResponse<SecurityModel>> response) {
                    BaseResponse.Error error = (BaseResponse.Error) response.body().getError();
                    if (error != null) {
                        requestCallback.failure(JsonManager.beanToJSONString(error));
                    } else {
                        String secStr = JsonManager.beanToJSONString(response.body().getData());
                        MySqlite.getInstance(context).insertData("security_" + securityId , secStr);
                        requestCallback.success(secStr);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<SecurityModel>> call, Throwable t) {
                    requestCallback.failure("error");
                }
            });
        }
    }
    /**
     *  获取社保卡信息
     * @param context
     * @param requestCallback
     */
    public static void getPersonInfo(final Context context, final RequestCallback requestCallback){
        RetrofitManager.getInstance(context).getRetrofit().create(MyServices.class).getPersonInfo().enqueue(new Callback<BaseResponse<PersonInfo>>() {
            @Override
            public void onResponse(Call<BaseResponse<PersonInfo>> call, Response<BaseResponse<PersonInfo>> response) {
                BaseResponse.Error error = (BaseResponse.Error)response.body().getError();
                if (error == null) {
                    requestCallback.success(JsonManager.beanToJSONString(response.body().getData()));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<PersonInfo>> call, Throwable t) {

            }
        });
    }
}
