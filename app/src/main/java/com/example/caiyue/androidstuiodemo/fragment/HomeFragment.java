package com.example.caiyue.androidstuiodemo.fragment;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caiyue.androidstuiodemo.R;
import com.example.caiyue.androidstuiodemo.http.GolablRequest;
import com.example.caiyue.androidstuiodemo.http.HttpUtils;
import com.example.caiyue.androidstuiodemo.http.MyServices;
import com.example.caiyue.androidstuiodemo.http.RetrofitManager;
import com.example.caiyue.androidstuiodemo.model.BaseResponse;
import com.example.caiyue.androidstuiodemo.model.IndexBannerModel;
import com.example.caiyue.androidstuiodemo.model.SecurityModel;
import com.example.caiyue.androidstuiodemo.model.SpecialModel;
import com.example.caiyue.androidstuiodemo.model.UserModel;
import com.example.caiyue.androidstuiodemo.myView.CheckBoxView;
import com.example.caiyue.androidstuiodemo.adapter.BannerAdapter;
import com.example.caiyue.androidstuiodemo.utils.JsonManager;
import com.example.caiyue.androidstuiodemo.utils.MySqlite;
import com.example.caiyue.androidstuiodemo.utils.SharedPreferencesManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener{
    private ViewPager viewPager;
    private LinearLayout pointLinearLayout;
    private CheckBoxView trans_balance;
    private int banners;
    private int curBannerIndex = 0;
    private List<View> pointsViewList = new ArrayList<View>();
    private static final String TAG = "home";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        LocalMap.getLocation(new LocalMap.MyLocationListener(getActivity()) {
//            @Override
//            public void scuess(AMapLocation location) {
//                Log.i(TAG, location.toString());
//            }
//        });
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        initData();
        super.onStart();
    }
    private void initData(){
        String userStr = MySqlite.getInstance(getActivity()).getData("userData");
        if (!userStr.isEmpty()) {
            // 获取用户的登录状态（如被踢了，就清除用户信息）
            GolablRequest.queryUser(getActivity(), new GolablRequest.RequestCallback() {
                @Override
                public void success(String str) {
                    UserModel newUserModel = JsonManager.stringToBean(str, UserModel.class);
                    if (newUserModel.getSecurityAcctId() != null) {
                        GolablRequest.getSecurityDataById(getActivity(), newUserModel.getSecurityAcctId(), false, new GolablRequest.RequestCallback() {
                            @Override
                            public void success(String str) {
                                Log.i(TAG, str);
                                initTopSecurityView(str);
                            }

                            @Override
                            public void failure(String str) {
                                Log.i(TAG, str);
                            }
                        });
                    }
                }

                @Override
                public void failure(String str) {
                    Log.i(TAG, str);
                }
            });
        }
        String specilStr = MySqlite.getInstance(getActivity()).getData( "special");
        if (!specilStr.isEmpty()) {
            SpecialModel specialModel = JsonManager.stringToBean(specilStr, SpecialModel.class);
            setSpecial(specialModel);
        } else {
            RetrofitManager.getInstance(getActivity()).getRetrofit().create(MyServices.class).getNewsetSpecial().enqueue(new Callback<BaseResponse<SpecialModel>>() {
                @Override
                public void onResponse(Call<BaseResponse<SpecialModel>> call, Response<BaseResponse<SpecialModel>> response) {
                    SpecialModel specialModel = response.body().getData();
                    setSpecial(specialModel);
                    MySqlite.getInstance(getActivity()).insertData("special", JsonManager.beanToJSONString(specialModel));
                }

                @Override
                public void onFailure(Call<BaseResponse<SpecialModel>> call, Throwable t) {

                }
            });
        }
        String bannerStr = MySqlite.getInstance(getActivity()).getData( "banner");
        if (!bannerStr.isEmpty()) {
            List<IndexBannerModel> listBannerModel = JsonManager.jsonStringToListBean(bannerStr, IndexBannerModel.class);
            initBanner(listBannerModel);
            initExtraNav(listBannerModel);
        } else {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("os", "android");
            RequestBody body = RetrofitManager.getRequstBodyFromMap(param);
            RetrofitManager.getInstance(getActivity()).getRetrofit().create(MyServices.class).getExtentList(body).enqueue(new Callback<BaseResponse<List<IndexBannerModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<IndexBannerModel>>> call, Response<BaseResponse<List<IndexBannerModel>>> response) {
                    initBanner(response.body().getData());
                    initExtraNav(response.body().getData());
                    MySqlite.getInstance(getActivity()).insertData("banner", JsonManager.listBeanToJsonString(response.body().getData()));
                }

                @Override
                public void onFailure(Call<BaseResponse<List<IndexBannerModel>>> call, Throwable t) {

                }
            });
        }
    }
    private void initTopSecurityView(String str){
        RelativeLayout security_check = getActivity().findViewById(R.id.security_check);
        RelativeLayout security_message = getActivity().findViewById(R.id.security_message);
        final SecurityModel securityModel = JsonManager.stringToBean(str, SecurityModel.class);
        if (securityModel != null) {
            security_check.setVisibility(View.GONE);
            security_message.setVisibility(View.VISIBLE);
            trans_balance = getActivity().findViewById(R.id.trans_balance);
            Boolean isShowBalance = (Boolean) SharedPreferencesManage.getIntsance(getActivity(),"showBalance").get("show", true);
            if (isShowBalance == null) {
                isShowBalance = true;
            }
            trans_balance.setCheckBoxChecked(isShowBalance);
            TextView card_name = getActivity().findViewById(R.id.card_name);
            TextView card_status = getActivity().findViewById(R.id.card_status);
            final TextView security_balance = getActivity().findViewById(R.id.security_balance);
            final TextView pension_balance = getActivity().findViewById(R.id.pension_balance);
            card_name.setText(securityModel.getName());
            card_status.setText(securityModel.getSecurityState());
            security_balance.setText( isShowBalance ? securityModel.getMedicalAccountBalance() : "****");
            pension_balance.setText(isShowBalance ? securityModel.getPensionAccountBalance() : "****");
            trans_balance.setCheckBoxViewClickListenler(new CheckBoxView.OnCheckBoxViewClickListener() {
                @Override
                public void checkBoxViewClicked(View v) {
                    trans_balance.setCheckBoxChecked(!trans_balance.isChecked());
                    SharedPreferencesManage.getIntsance(getActivity(),"showBalance").add("show", trans_balance.isChecked());
                    security_balance.setText( trans_balance.isChecked() ? securityModel.getMedicalAccountBalance() : "****");
                    pension_balance.setText(trans_balance.isChecked() ? securityModel.getPensionAccountBalance() : "****");
                }
            });
        }
    }
    private void initExtraNav(List<IndexBannerModel> list){
        LinearLayout extraLinearLayout = getActivity().findViewById(R.id.extra_nav);
        extraLinearLayout.removeAllViews();
        for (int i = 0; i < list.size(); i ++) {
            final IndexBannerModel indexBannerModel = list.get(i);
            LinearLayout linearLayout = (LinearLayout) LinearLayout.inflate(getActivity(),R.layout.nav_icon_tit,null);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            ImageView imageView = linearLayout.findViewById(R.id.nav_image);
            TextView textView = linearLayout.findViewById(R.id.nav_tit);
            HttpUtils.setImage(indexBannerModel.getIconPath(), imageView);
            textView.setText(indexBannerModel.getName());
            extraLinearLayout.addView(linearLayout);
            linearLayout.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), indexBannerModel.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /**
     * 初始化 banner
     */
    private void initBanner(List<IndexBannerModel> list){

        List<View> viewList = new ArrayList<View>();
        banners = list.size();
        for (int i = 0; i < list.size(); i ++) {
            final IndexBannerModel indexBannerModel = list.get(i);
            ImageView imageView = new ImageView(getActivity());
            imageView.setAdjustViewBounds(true);
            HttpUtils.setImage(indexBannerModel.getIconPath(), imageView);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "" + indexBannerModel.getLinkUrl(), Toast.LENGTH_LONG).show();
                }
            });
            viewList.add(imageView);
        }
        BannerAdapter bannerAdapter = new BannerAdapter(viewList);
        viewPager = getActivity().findViewById(R.id.banner);
        viewPager.setAdapter(bannerAdapter);
        initPoint();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setCurrentPoints(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 轮播图的底部指示器
     */
    private void initPoint(){
        pointLinearLayout = getActivity().findViewById(R.id.points);
        for (int i = 0; i < banners; i ++) {
            ImageView imageView = new ImageView(getActivity());

            imageView.setLayoutParams(new ViewGroup.LayoutParams(15,
                    15));

            if (curBannerIndex == i) {
                imageView.setBackgroundResource(R.drawable.point_cur);
            } else {
                imageView.setBackgroundResource(R.drawable.point_default);
            }
            pointLinearLayout.addView(imageView);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
            p.setMargins(5,5,5,5);
            imageView.requestLayout();
            pointsViewList.add(imageView);
        }
    }
    private void setCurrentPoints(int curBannerIndex){
        for (int i = 0; i < pointLinearLayout.getChildCount(); i ++) {
            pointsViewList.get(i).setBackgroundResource(R.drawable.point_default);
        }
        pointsViewList.get(curBannerIndex).setBackgroundResource(R.drawable.point_cur);
    }
    private void setSpecial(SpecialModel specialModel){
        final ImageView imageView = getActivity().findViewById(R.id.special_img);
        TextView term = getActivity().findViewById(R.id.special_term);
        TextView date = getActivity().findViewById(R.id.special_date);
        TextView title = getActivity().findViewById(R.id.special_title);
        BitmapFactory.Options options = new BitmapFactory.Options();
        HttpUtils.setImage(specialModel.getImageUrl(), imageView);
        term.setText("第" + specialModel.getSerialNumber() + "期");
        date.setText(specialModel.getMonth());
        title.setText(specialModel.getTitle());
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, v.getId()+ ":id");
        switch (v.getId()) {
            case R.id.special_img:
                Toast.makeText(getActivity(), "imgClick", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
