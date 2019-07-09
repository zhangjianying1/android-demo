package com.example.caiyue.androidstuiodemo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.caiyue.androidstuiodemo.MainActivity;
import com.example.caiyue.androidstuiodemo.R;
import com.example.caiyue.androidstuiodemo.adapter.BannerAdapter;
import com.example.caiyue.androidstuiodemo.adapter.GuideAdapter;
import com.example.caiyue.androidstuiodemo.model.AdModel;
import com.example.caiyue.androidstuiodemo.utils.ImageLoadManager;
import com.example.caiyue.androidstuiodemo.utils.JsonManager;
import com.example.caiyue.androidstuiodemo.utils.DisklrucacheHelper;
import com.example.caiyue.androidstuiodemo.utils.MySqlite;
import com.example.caiyue.androidstuiodemo.utils.SharedPreferencesManage;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {
    private ImageView imageView;
    private Handler handler;
    private Runnable runnable;
    private int count = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        imageView = findViewById(R.id.welcome_image);
        String d = "mipmap";
        String s = "ic_spalsh";
        String pageName = getPackageName();
        try {
            // 加载资源文件
            int imgId = Class.forName(pageName + ".R$" + d).getField(s).getInt(null);
            imageView.setImageResource(imgId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.05f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.05f);
        AnimatorSet anSet = new AnimatorSet();
        anSet.setDuration(2000).play(animatorX).with(animatorY);
        anSet.start();
        anSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                adFilter();
            }
        });
    }
    private Bitmap readBitmapfromFile(String path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / srcWidth);
            } else {
                inSampleSize = Math.round(srcWidth / srcHeight);
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path, options);
    }
    // 广告
    private void adFilter(){
        String adStr = MySqlite.getInstance(this).getData("splashAD");
        AdModel adModel = JsonManager.stringToBean(adStr, AdModel.class);
        boolean visited = (Boolean) SharedPreferencesManage.getIntsance(WelcomeActivity.this, "firstWelcome").get("visited", true);
        Log.i("wel", visited + ":is");
        if (visited == true) {
            showGuideView();
        } else if (adModel != null) {
            AdModel.MaterielModel materielModel = adModel.getAdInfoList().get(0);
            count = Integer.parseInt(adModel.getShowTime());
            showAd(materielModel);
        } else {
            startMain();
        }
    }
    // 显示引导图
    private void showGuideView() {
        ViewPager viewPager = findViewById(R.id.guide_pager);
        BannerAdapter bannerAdapter = new BannerAdapter(createView());
        viewPager.setAdapter(bannerAdapter);
    }
    private List<View> createView(){
        List<View> listView = new ArrayList<View>();
        Button button;
        int[] guideImage = { R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4 };
        for (int i = 0; i < guideImage.length; i ++) {
            RelativeLayout template = (RelativeLayout) View.inflate(this, R.layout.template_guide, null);
            ImageView imageView = template.findViewById(R.id.guide_image);
            button = template.findViewById(R.id.experience_button);
            imageView.setImageResource(guideImage[i]);
            if (i == guideImage.length - 1) {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startMain();
                    }
                });
            }
            listView.add(template);
        }

        return listView;
    }
    private void showAd(final AdModel.MaterielModel materielModel){
        final RelativeLayout adRelativeLayout = findViewById(R.id.ad);
        ImageView adImage = findViewById(R.id.ad_image);
        TextView textViewTimeout = findViewById(R.id.time_count);
        ImageLoadManager.getInstance().showImage(adImage, materielModel.getAdImgPath());
        adRelativeLayout.setVisibility(View.VISIBLE);
        downCountTime(textViewTimeout);
        adRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, WebviewActivity.class);
                intent.putExtra("url", materielModel.getAdTargetPath());
                intent.putExtra("prevView", "welcome");
                startActivity(intent);
                finish();
            }
        });
        textViewTimeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain();
            }
        });
    }
    private void downCountTime(final TextView textViewTimeout){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    textViewTimeout.setText("跳过" + msg.arg1);
                }
            }
        };
        runnable = new Runnable() {
            @Override
            public void run() {
                count --;
                if (count == 0) {
                    startMain();
                    handler.removeCallbacks(runnable);
                    return;
                }
                handler.postDelayed(this, 1000);
                Message message = new Message();
                message.what = 0;
                message.arg1 = count;
                handler.sendMessage(message);
            }
        };
        handler.post(runnable);
    }
    private void startMain(){
        SharedPreferencesManage.getIntsance(WelcomeActivity.this, "firstWelcome").add("visited", true);
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        // 解决进入main页布局下移的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null)
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ImageLoadManager.getInstance().closeLoadImage();
    }
}
