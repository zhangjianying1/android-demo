package com.example.caiyue.androidstuiodemo.myDrawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CircleImage extends Drawable {
    private Bitmap bitmap;
    private int size;
    private int radius;
    private Paint mPaint;
    private BitmapShader bitmapShader;

    public CircleImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        radius = size/2;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawCircle(radius, radius, radius, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
    @Override
    public int getIntrinsicHeight() {
        return size;
    }

    @Override
    public int getIntrinsicWidth() {
        return size;
    }

}
