package com.example.caiyue.androidstuiodemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caiyue.androidstuiodemo.fragment.ArticleFragment;
import com.example.caiyue.androidstuiodemo.fragment.HomeFragment;
import com.example.caiyue.androidstuiodemo.fragment.IntelligentFragment;
import com.example.caiyue.androidstuiodemo.fragment.ServiceFragment;
import com.example.caiyue.androidstuiodemo.fragment.UserFragment;
import com.example.caiyue.androidstuiodemo.http.MyServices;
import com.example.caiyue.androidstuiodemo.http.RetrofitManager;
import com.example.caiyue.androidstuiodemo.model.AdModel;
import com.example.caiyue.androidstuiodemo.model.BaseResponse;
import com.example.caiyue.androidstuiodemo.services.IntentService;
import com.example.caiyue.androidstuiodemo.services.PushService;
import com.example.caiyue.androidstuiodemo.utils.AppInfo;
import com.example.caiyue.androidstuiodemo.utils.ImageLoadManager;
import com.example.caiyue.androidstuiodemo.utils.JsonManager;
import com.example.caiyue.androidstuiodemo.utils.DisklrucacheHelper;
import com.example.caiyue.androidstuiodemo.utils.MySqlite;
import com.igexin.sdk.PushManager;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private View tabUser, tabHome, tabAticle, tabIntelligent, tabService;
    private FragmentManager fmMannger;
    private Fragment homeFragment, articleFragment, intelligentFragment, serciveFragment, userFragment;
    private ImageView homeImg, articleImg, userImg, serviceImg;
    private TextView homeText, articleText, intelligentText, serviceText, userText;
    FragmentTransaction transaction;
    final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        // 初始化个推
        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        // 注册回执服务
        PushManager.getInstance().registerPushIntentService(MainActivity.this, IntentService.class);
    }
    private void initView(){
        tabHome = findViewById(R.id.home);
        tabAticle = findViewById(R.id.article);
        tabUser = findViewById(R.id.user);
        tabIntelligent = findViewById(R.id.intelligent);
        tabService = findViewById(R.id.service);
        homeText = findViewById(R.id.home_text);
        articleText = findViewById(R.id.article_text);
        intelligentText = findViewById(R.id.intelligent_text);
        serviceText = findViewById(R.id.service_text);
        userText = findViewById(R.id.user_text);
        homeImg = findViewById(R.id.home_imageView);
        articleImg = findViewById(R.id.article_image);
        serviceImg = findViewById(R.id.service_image);
        userImg = findViewById(R.id.user_image);
        tabHome.setOnClickListener(this);
        tabAticle.setOnClickListener(this);
        tabIntelligent.setOnClickListener(this);
        tabService.setOnClickListener(this);
        tabUser.setOnClickListener(this);
        fmMannger  = getFragmentManager();
        transaction = fmMannger.beginTransaction();
        homeFragment = new HomeFragment();
        transaction.add(R.id.frame_layout, homeFragment);
        homeText.setTextColor(getResources().getColor(R.color.green));
        transaction.commit();
        requestAdInfo();
    }

    /**
     * 请求广告并保存图片至本地
     */
    private void requestAdInfo(){
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> appInfo = AppInfo.getAppInfo(this);
        param.put("appid", appInfo.get("appId"));
        param.put("adLocationId", "guide");
        param.put("resVersion", appInfo.get("versionName"));
        param.put("appVersion", appInfo.get("versionName"));
        param.put("origin", "default");
        param.put("androidId", appInfo.get("deviceId"));
        param.put("channel", "test");
        param.put("deviceModel", appInfo.get("model"));
        param.put("deviceVender", appInfo.get("vender"));
        param.put("os", "Android");
        param.put("osLanguage", appInfo.get("osLanguage"));
        param.put("osVersion", appInfo.get("os"));
        param.put("cityCode", "110000");
        param.put("cityName", "北京");
        param.put("deviceId", appInfo.get("deviceId"));
        param.put("deviceImei", appInfo.get("deviceId"));
        param.put("geTuiId", null);
        param.put("advId", 2);
        RequestBody requestBody = RetrofitManager.getRequstBodyFromMap(param);
        RetrofitManager.getInstance(this).getRetrofit().create(MyServices.class).getAdInfo(requestBody).enqueue(new Callback<BaseResponse<AdModel>>() {
            @Override
            public void onResponse(Call<BaseResponse<AdModel>> call, Response<BaseResponse<AdModel>> response) {
                saveAdInfo(response.body().getData());
            }

            @Override
            public void onFailure(Call<BaseResponse<AdModel>> call, Throwable t) {

            }
        });
    }
    private void saveAdInfo(final AdModel adModel){
        if (adModel.getAdInfoList() != null && adModel.getAdInfoList().size() > 0) {
            final AdModel.MaterielModel materielModel = adModel.getAdInfoList().get(0);
            if (materielModel != null) {
                // 缓存图片到本地
                ImageLoadManager.getInstance().showImage(null, "http://192.168.1.223:8082/upload/advsystem/image/201808/9ae332e9cbf8489784f1a8935e8eb70e.jpg");
                MySqlite.getInstance(this).insertData("splashAD", JsonManager.beanToJSONString(adModel));
            }
        } else {
            // 删除缓存
            MySqlite.getInstance(MainActivity.this).delete("splashAD");
        }
    }
    @Override
    public void onClick(View v) {
        transaction = fmMannger.beginTransaction();
        hideAllFragment(transaction);
        setTabViewDefault();
        switch (v.getId()) {
            case R.id.home:
                homeText.setTextColor(getResources().getColor(R.color.green));
                homeImg.setImageResource(R.drawable.home_a);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.frame_layout, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case R.id.article:
                articleText.setTextColor(getResources().getColor(R.color.green));
                articleImg.setImageResource(R.drawable.article_a);
                if (articleFragment == null) {
                    articleFragment = new ArticleFragment();
                    transaction.add(R.id.frame_layout, articleFragment);
                } else {
                    transaction.show(articleFragment);
                }
                break;
            case R.id.intelligent:
                intelligentText.setTextColor(getResources().getColor(R.color.green));
                if (intelligentFragment == null) {
                    intelligentFragment = new IntelligentFragment();
                    transaction.add(R.id.frame_layout, intelligentFragment);
                } else {
                    transaction.show(intelligentFragment);
                }
                break;
            case R.id.service:
                serviceText.setTextColor(getResources().getColor(R.color.green));
                serviceImg.setImageResource(R.drawable.service_a);
                if (serciveFragment == null) {
                    serciveFragment = new ServiceFragment();
                    transaction.add(R.id.frame_layout, serciveFragment);
                } else {
                    transaction.show(serciveFragment);
                }
                break;
            case R.id.user:
                userText.setTextColor(getResources().getColor(R.color.green));
                userImg.setImageResource(R.drawable.user_a);
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    transaction.add(R.id.frame_layout, userFragment);
                } else {
                    transaction.show(userFragment);
                }
                break;
            //default
        }
        transaction.commit();
    }
    private void hideAllFragment(FragmentTransaction transaction){
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (articleFragment != null) {
            transaction.hide(articleFragment);
        }
        if (intelligentFragment != null) {
            transaction.hide(intelligentFragment);
        }
        if (serciveFragment != null) {
            transaction.hide(serciveFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }
    private void setTabViewDefault(){
        homeText.setTextColor(getResources().getColor((R.color.gray)));
        articleText.setTextColor(getResources().getColor((R.color.gray)));
        intelligentText.setTextColor(getResources().getColor(R.color.gray));
        serviceText.setTextColor(getResources().getColor(R.color.gray));
        userText.setTextColor(getResources().getColor((R.color.gray)));
        homeImg.setImageResource(R.drawable.user);
        articleImg.setImageResource(R.drawable.article);
        serviceImg.setImageResource(R.drawable.service);
        userImg.setImageResource(R.drawable.user);
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {

            }
        }
    };
    boolean willExit = false;
    @Override
    public void onBackPressed() {
       Runnable resetFlags = new Runnable() {
           @Override
           public void run() {
               willExit = false;
           }
       };
       if (!willExit) {
           handler.postDelayed(resetFlags, 2*1000);
           willExit = true;
           Toast.makeText(this, "再按一次退出应用程序", Toast.LENGTH_SHORT).show();
       } else {
           handler.removeCallbacks(resetFlags);
           finish();
       }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ImageLoadManager.getInstance().closeLoadImage();
    }
}
