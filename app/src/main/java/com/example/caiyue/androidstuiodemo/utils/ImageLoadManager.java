package com.example.caiyue.androidstuiodemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.caiyue.androidstuiodemo.MainApplication;
import com.example.caiyue.androidstuiodemo.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoadManager {
    private String url;
    private DisklrucacheHelper disklrucacheHelper;
    private static ImageLoadManager imageLoadManager;

    public ImageLoadManager() {
        disklrucacheHelper = new DisklrucacheHelper(MainApplication.getContext());
    }

    public static ImageLoadManager getInstance() {
        if (imageLoadManager == null) {
            synchronized (ImageLoadManager.class) {
                imageLoadManager = new ImageLoadManager();
            }
        }
        return imageLoadManager;
    }
    public void showImage(ImageView imageView, String path){
        InputStream inputStream = getCacheByPath(path);
        if (inputStream != null) {
            if (imageView != null) {
                imageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            }
        } else {
            downloadImage(path);
        }
    }
    public void closeLoadImage(){
        disklrucacheHelper.closeDiskLruCache();
    }
    private InputStream getCacheByPath(String path) {
        return disklrucacheHelper.getCache(path);
    }
    HttpURLConnection urlConnection = null;
    private void downloadImage(final String path){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final URL url = new URL(path);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(3000);
                    if (urlConnection.getResponseCode() == 200) {
                        disklrucacheHelper.setCache(path, urlConnection.getInputStream());
                    }

                } catch (Exception e) {
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }
}
