package com.example.caiyue.androidstuiodemo.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caiyue.androidstuiodemo.R;
import com.example.caiyue.androidstuiodemo.utils.MySqlite;
import com.example.caiyue.androidstuiodemo.utils.SharedPreferencesManage;
import com.example.caiyue.androidstuiodemo.http.HttpUtils;
import com.example.caiyue.androidstuiodemo.http.MyServices;
import com.example.caiyue.androidstuiodemo.http.RetrofitManager;
import com.example.caiyue.androidstuiodemo.model.BaseResponse;
import com.example.caiyue.androidstuiodemo.model.UserModel;
import com.example.caiyue.androidstuiodemo.utils.JsonManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText mobileEditText, messageEditText;
    private TextView sendCodeTextView;
    private String mobileText, messageText;
    private Button submitButton;
    private HttpUtils httpUtils;
    private Handler handler;
    private static final String TAG = "login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                final String response = (String) msg.obj;
                if (msg.what == 1) {
                    SharedPreferencesManage sharedPreferencesManage = new SharedPreferencesManage(LoginActivity.this, "USER");
                    sharedPreferencesManage.add("USER", response);
                    Intent intent = new Intent();
                    intent.putExtra("userData", response);
                    setResult(1, intent);
                    finish();
                } else if (msg.what == 0) {
                    Toast.makeText(LoginActivity.this, "发送短信验证码", Toast.LENGTH_LONG).show();
                }
            }
        };
        getProcessName(LoginActivity.this, android.os.Process.myPid());
    }
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();

        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            Log.i(TAG, procInfo.pid + "process" + pid +"processName"+ cxt.getPackageName());
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra("secondePage", "login");
        setResult(RESULT_OK, intent);
        finish();
    }
    private void initView(){
        mobileEditText = findViewById(R.id.mobile);
        messageEditText = findViewById(R.id.message);
        submitButton = findViewById(R.id.submit_button_login);
        sendCodeTextView = findViewById(R.id.send_code_text);
        submitButton.setOnClickListener(this);
        sendCodeTextView.setOnClickListener(this);
        mobileEditText.setText((String)SharedPreferencesManage.getIntsance(LoginActivity.this, "login").get("mobile", ""));
        messageEditText.setText((String)SharedPreferencesManage.getIntsance(LoginActivity.this, "login").get("message", ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button_login:
                loginHandle();
                break;
            case R.id.send_code_text:
                sendCodeHandle();
                break;
            // default:
        }
    }
    private void loginHandle(){
        Map<String, Object> requestParams = new HashMap<String, Object>();
        mobileText = mobileEditText.getText().toString();
        messageText = messageEditText.getText().toString();
        String mobileRE = "^1[23456789]\\d{9}$";
        String errorText = null;
        Pattern p = Pattern.compile(mobileRE);
        final Matcher m = p.matcher(mobileText);
        boolean isMatch = m.matches();
        // 创建 Pattern 对象
        if (mobileText.trim().isEmpty()) {
            errorText = "手机号码不能为空";
        } else if (!isMatch) {
            errorText = "手机号码不正确";
        }
        // 创建 Pattern 对象
        if (messageText.trim().isEmpty()) {
            errorText = "短信验证码不能为空";
        }
        if (null != errorText) {
            Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
        } else {
            requestParams.put("mobile", mobileText);
            requestParams.put("rcode", messageText);
            requestParams.put("appVersion", "1.7.5");
            requestParams.put("resVersion", "1.7.5");
            requestParams.put("cityCode", "110000");
            requestParams.put("deviceModel", messageText);
            requestParams.put("osVersion", messageText);
            requestParams.put("osLanguage", messageText);
            requestParams.put("deviceId", messageText);
            RequestBody body = RetrofitManager.getRequstBodyFromMap(requestParams);
            RetrofitManager.getInstance(LoginActivity.this).getRetrofit().create(MyServices.class).login(body).enqueue(new Callback<BaseResponse<UserModel>>() {
                @Override
                public void onResponse(Call<BaseResponse<UserModel>> call, Response<BaseResponse<UserModel>> response) {
                    Log.i(TAG, response.toString());

                    if (response.body().getError() == null) {
                        SharedPreferencesManage.getIntsance(LoginActivity.this, "login").add("mobile", mobileText);
                        SharedPreferencesManage.getIntsance(LoginActivity.this, "login").add("message", messageText);
                        MySqlite.getInstance(LoginActivity.this).insertData("userData", JsonManager.beanToJSONString(response.body().getData()));
                        Intent intent = new Intent();
                        intent.putExtra("userData", (Serializable)response.body().getData());
                        setResult(1, intent);
                        finish();
                    } else {
                        JsonObject errorJsonObject = JsonManager.stringToJson(response.body().getError().toString());
                        Toast.makeText(LoginActivity.this, errorJsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<UserModel>> call, Throwable t) {

                }
            });
        }
    }
    private void sendCodeHandle(){
        String errorText = null;
        Map<String, Object> requestParam = new HashMap<String, Object>();
        mobileText = mobileEditText.getText().toString();
        Pattern pattern = Pattern.compile("^1[23456789]\\d{9}$");
        Matcher matcher = pattern.matcher(mobileText);
        if (mobileText.isEmpty()) {
            errorText = "手机号码不能为空";
        } else if (!matcher.matches()) {
            errorText = "手机号码错误";
        }
        if (errorText != null) {
            Toast.makeText(LoginActivity.this, errorText, Toast.LENGTH_SHORT).show();
        } else {

            requestParam.put("REG_AuthCode", "REG_AuthCode");
            requestParam.put("mobile", mobileText);
            RequestBody requestBody = RetrofitManager.getRequstBodyFromMap(requestParam);
            RetrofitManager.getInstance(LoginActivity.this).getRetrofit().create(MyServices.class).sendOtpMessage(requestBody).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    new CountDownTimer(60000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            sendCodeTextView.setEnabled(false);
                            sendCodeTextView.setText(millisUntilFinished/1000 + "秒");
                            sendCodeTextView.setTextColor(getResources().getColor(R.color.colorDDD));
                        }

                        @Override
                        public void onFinish() {
                            sendCodeTextView.setEnabled(true);
                            sendCodeTextView.setTextColor(getResources().getColor(R.color.colorWhite));
                        }
                    }.start();
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {

                }
            });
        }

    }


}
