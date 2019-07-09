package com.example.caiyue.androidstuiodemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.caiyue.androidstuiodemo.MainActivity;
import com.example.caiyue.androidstuiodemo.R;

public class WebviewActivity extends Activity {
    private WebView webView;
    private String prevView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.webview);
        loadWebviewClient();
    }
    private void loadWebviewClient(){
        String url = getIntent().getStringExtra("url");
        prevView = getIntent().getStringExtra("prevView");
        Log.i("web", url + ":url");
        WebSettings webSettings = webView.getSettings();
//支持缩放，默认为true。
        webSettings .setSupportZoom(false);
//调整图片至适合webview的大小
        webSettings .setUseWideViewPort(true);
// 缩放至屏幕的大小
        webSettings .setLoadWithOverviewMode(true);
//设置默认编码
        webSettings .setDefaultTextEncodingName("utf-8");
////设置自动加载图片
        webSettings .setLoadsImagesAutomatically(true);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("wel", (prevView.equals("welcome")) + "f");
        if (prevView.equals("welcome")) {
            Log.i("web", "back");
            startActivity(new Intent(WebviewActivity.this, MainActivity.class));
        }
    }
}
