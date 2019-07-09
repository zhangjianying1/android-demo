package com.example.caiyue.androidstuiodemo.http;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.caiyue.androidstuiodemo.MainApplication;
import com.example.caiyue.androidstuiodemo.utils.AppInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class HttpUtils {
    private static final String TAG = "request";
    private static Map<String, Object> appInfo;
    public static interface OnRequestListener {
        void onSucess(JsonObject response);
        void onError(JsonObject error);
    }
    public static byte[] getByte(InputStream ins) throws Exception {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int len = 0;

        while ((len = ins.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, buffer.length);
        }
        return byteArrayOutputStream.toByteArray();
    }
    public static void setImage(final String path, final ImageView imageView){

        final Handler handler = new Handler(){
            public void handleMessage(Message message){
                Bitmap bitmap = (Bitmap) message.obj;
                imageView.setImageBitmap(bitmap);
            }
        };
        Runnable network =  new Runnable(){

            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);

                    if (200 != conn.getResponseCode()) {
                        throw new Exception("加载失败...");
                    }
                    InputStream is = conn.getInputStream();
                    Log.i("main", is.toString());
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        new Thread(network).start();

    }
    public static void getImageBitmap(final String imageUrl, final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() != 200) {
                        Log.i(TAG, "图片加载失败");
                    }
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    conn.disconnect();
                    Message message = new Message();
                    message.what = 0;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
