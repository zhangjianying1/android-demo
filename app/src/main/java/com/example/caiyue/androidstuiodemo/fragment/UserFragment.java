package com.example.caiyue.androidstuiodemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.caiyue.androidstuiodemo.R;
import com.example.caiyue.androidstuiodemo.activity.LoginActivity;
import com.example.caiyue.androidstuiodemo.http.GolablRequest;
import com.example.caiyue.androidstuiodemo.model.PersonInfo;
import com.example.caiyue.androidstuiodemo.utils.JsonManager;
import com.example.caiyue.androidstuiodemo.utils.MySqlite;
import com.example.caiyue.androidstuiodemo.utils.SharedPreferencesManage;
import com.example.caiyue.androidstuiodemo.http.HttpUtils;
import com.example.caiyue.androidstuiodemo.http.MyServices;
import com.example.caiyue.androidstuiodemo.http.RetrofitManager;
import com.example.caiyue.androidstuiodemo.model.BaseResponse;
import com.example.caiyue.androidstuiodemo.model.UserModel;
import com.example.caiyue.androidstuiodemo.myDrawable.CircleImage;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment implements View.OnClickListener{
    private final static String TAG = "account";
    private LinearLayout header;
    private TextView userName, userMobile;
    private ImageView userPhoto;
    private ImageButton editButton;
    private HttpUtils httpUtils;
    private Handler handler;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, 1);
                break;
            // default
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UserModel userModel = (UserModel) data.getSerializableExtra("userData");
        handleUser(userModel);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void initView(View view){
        header = view.findViewById(R.id.header);
        userPhoto = view.findViewById(R.id.userPhoto);
        userName = view.findViewById(R.id.user_name);
        userMobile = view.findViewById(R.id.user_mobile);
        editButton = view.findViewById(R.id.edit_user);
        handleUser(null);
        header.setOnClickListener(this);
    }
    private void handleUser(UserModel userModel) {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (0 == msg.what) {
                    userPhoto.setImageDrawable(new CircleImage((Bitmap) msg.obj));
                }
            }
        };
        if (null == userModel)  {
            String userStr = MySqlite.getInstance(getActivity()).getData("userData");
            Log.i(TAG, userStr);
            if (userStr != null) {
                userModel = JsonManager.stringToBean(userStr, UserModel.class);
            }
        }
        if (null != userModel) {
            userName.setText(userModel.getUserName());
            userMobile.setText(userModel.getMobile());
            HttpUtils.getImageBitmap(userModel.getHeadPath(), handler);
            GolablRequest.getPersonInfo(getActivity(), new GolablRequest.RequestCallback() {
                @Override
                public void success(String str) {
                    Log.i(TAG, str);
                    initPersonInfo(str);
                }

                @Override
                public void failure(String str) {

                }
            });
        } else {
            userPhoto.setImageDrawable(new CircleImage(BitmapFactory.decodeResource(getResources(),R.drawable.header)));
        }
    }
    private void initPersonInfo(String jsonString){
        TextView secuirty_num, fund_num;
        secuirty_num = getActivity().findViewById(R.id.secuirty_num);
        fund_num = getActivity().findViewById(R.id.fund_num);
        PersonInfo personInfo = JsonManager.stringToBean(jsonString, PersonInfo.class);
        secuirty_num.setText(personInfo.getJoyCardNum());
        fund_num.setText(personInfo.getJoyFundNum());
    }
}
