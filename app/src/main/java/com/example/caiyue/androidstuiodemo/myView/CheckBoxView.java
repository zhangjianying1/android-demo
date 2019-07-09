package com.example.caiyue.androidstuiodemo.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
 import android.widget.TextView;

import com.example.caiyue.androidstuiodemo.R;

public class CheckBoxView extends LinearLayout {
    private String desc_off;
    private String desc_on;
    private CheckBox checkBox;
    private TextView textView;
    private int startRawX;
    private int startRawY;
    private OnCheckBoxViewClickListener onCheckBoxViewClickListener;
    public CheckBoxView(Context context) {
        super(context, null);
    }

    public CheckBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckBoxView);
        desc_off = typedArray.getString(R.styleable.CheckBoxView_desc_off);
        desc_on = typedArray.getString(R.styleable.CheckBoxView_desc_on);
        typedArray.recycle();
        initView(context);
    }

    public CheckBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.check_box_eye, null);
        checkBox = linearLayout.findViewById(R.id.check_eye_trans);
        textView = linearLayout.findViewById(R.id.check_eye_trans_text);
        addView(linearLayout, 0);
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckBoxViewClickListener.checkBoxViewClicked(v);
            }
        });
    }
    public Boolean isChecked(){
        return checkBox.isChecked();
    }
    public void setCheckBoxChecked(Boolean checked){
        checkBox.setChecked(checked);
        textView.setText(checked ? desc_on : desc_off);
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_DOWN:
//                float x=event.getX();
//                float y = event.getY();
//                Log.i("home", x +":"+y);
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
    //定义接口的成员

    //接口的setter
    public void setCheckBoxViewClickListenler(OnCheckBoxViewClickListener onListener){
        onCheckBoxViewClickListener = onListener;
    }


    //接口
    public interface OnCheckBoxViewClickListener{
        public void checkBoxViewClicked(View v);//传的参数
    }
}
