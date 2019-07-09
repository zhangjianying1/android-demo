package com.example.caiyue.androidstuiodemo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.caiyue.androidstuiodemo.OverrideMethod.CenterLayoutManager;
import com.example.caiyue.androidstuiodemo.R;
import com.example.caiyue.androidstuiodemo.http.MyServices;
import com.example.caiyue.androidstuiodemo.http.RetrofitManager;
import com.example.caiyue.androidstuiodemo.model.BaseResponse;
import com.example.caiyue.androidstuiodemo.model.ChannelModel;
import com.example.caiyue.androidstuiodemo.model.UserModel;
import com.example.caiyue.androidstuiodemo.myView.PopWindowManager;
import com.example.caiyue.androidstuiodemo.adapter.ArticleNavAdapter;
import com.example.caiyue.androidstuiodemo.adapter.SelectedChannelAdapter;
import com.example.caiyue.androidstuiodemo.utils.MyStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.caiyue.androidstuiodemo.R.*;

public class ArticleFragment extends Fragment implements View.OnClickListener{
    private LinearLayout searchLinearLayout;
    private RecyclerView recyclerViewNav;
    private LinearLayout showPopViewBtn;
    private List<ChannelModel> navList;
    private List<ChannelModel> channelList;
    private List<ChannelModel> followChannelList = new ArrayList<ChannelModel>();
    private CountDownLatch countDownLatch = new CountDownLatch(2);
    private PopWindowManager popWindowManger;
    private UserModel userData = MyStorage.getInstance(getActivity()).getUserData();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_article, null);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view){
        searchLinearLayout = view.findViewById(id.search_btn);
        showPopViewBtn = view.findViewById(id.show_pop_view);
        recyclerViewNav = view.findViewById(id.article_nav_list);
        LinearLayoutManager lm = new CenterLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewNav.setLayoutManager(lm);
        showPopViewBtn.setOnClickListener(this);
        initData();
    }
    // 初始化数据
    private void initData(){
        RetrofitManager.getInstance(getActivity()).getRetrofit().create(MyServices.class).getChannelList().enqueue(new Callback<BaseResponse<List<ChannelModel>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ChannelModel>>> call, Response<BaseResponse<List<ChannelModel>>> response) {
                BaseResponse.Error error = (BaseResponse.Error) response.body().getError();
                countDownLatch.countDown();
                // 获取成功
                if (error == null) {
                    channelList = setNavSelectorChannelList(response.body().getData(), 0);
                    showRecycleView();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ChannelModel>>> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        if (userData != null) {
            RetrofitManager.getInstance(getActivity()).getRetrofit().create(MyServices.class).getFollowChannelList().enqueue(new Callback<BaseResponse<List<ChannelModel>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<ChannelModel>>> call, Response<BaseResponse<List<ChannelModel>>> response) {
                    BaseResponse.Error error = (BaseResponse.Error) response.body().getError();
                    countDownLatch.countDown();
                    // 获取成功
                    if (error == null) {
                        followChannelList = setNavSelectorChannelList(response.body().getData(), 0);
                        showRecycleView();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<ChannelModel>>> call, Throwable t) {
                    countDownLatch.countDown();
                }
            });
        } else {
            countDownLatch.countDown();
            showRecycleView();
        }

    }
    private List<ChannelModel> setNavSelectorChannelList(List<ChannelModel> listChannel, int index){
        for (int i = 0;i < listChannel.size(); i ++) {
            ChannelModel channelModel = listChannel.get(i);
            if (i == 0) {
                channelModel.setIsSelected(true);
            } else {
                channelModel.setIsSelected(false);
            }
        }
        return listChannel;
    }


    private void showRecycleView(){
        if (countDownLatch.getCount() == 0) {
            navList = channelList;

            if (followChannelList.size() > 0) {
                navList = followChannelList;
            }
            final ArticleNavAdapter adapter = new ArticleNavAdapter(navList);
            recyclerViewNav.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnRecyclerViewItemClickListener(new ArticleNavAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    for (int i = 0;i < navList.size(); i ++) {
                        ChannelModel channelModel = navList.get(i);
                        if (i == position) {
                            channelModel.setIsSelected(true);
                        } else {
                            channelModel.setIsSelected(false);
                        }
                    }
                    recyclerViewNav.smoothScrollToPosition(position);
                    adapter.notifyDataSetChanged();
                }
            });
            initPopupWindow();
        }
    }
    private void initPopupWindow() {
        DisplayMetrics metrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight=getActivity().getResources().getDisplayMetrics().heightPixels;
        popWindowManger=new PopWindowManager(getActivity(), layout.popup_widnow_view_from_bottom, ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight*0.7)) {
            @Override
            protected void initView() {
                View view=getContentView();
                initPopWindow(view);
            }

            @Override
            protected void initEvent() {
                View view=getContentView();
                LinearLayout closeLayout = view.findViewById(R.id.close_popview);
                closeLayout.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        popWindowManger.getPopupWindow().dismiss();
                    }
                });
            }

            @Override
            protected void initWindow() {
                super.initWindow();
                PopupWindow instance=getPopupWindow();
                instance.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
                        lp.alpha=1.0f;
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        getActivity().getWindow().setAttributes(lp);
                    }
                });
            }
        };
    }
    // 初始化popWindow的视图
    private void initPopWindow(View view){
        GridView selectedGrid = view.findViewById(R.id.gridview_all);
        List<ChannelModel> newChannelList;
        Log.i("art", followChannelList.toString());
        SelectedChannelAdapter adpter = new SelectedChannelAdapter(getActivity(), followChannelList);
        selectedGrid.setAdapter(adpter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case id.show_pop_view:
                if (countDownLatch.getCount() == 0) {
                    PopupWindow popupWindow = popWindowManger.getPopupWindow();
                    popupWindow.setAnimationStyle(style.Animation);
                    FrameLayout article_group = getActivity().findViewById(id.article_group);
                    popupWindow.showAtLocation(article_group, Gravity.BOTTOM, 0, 0);
                    WindowManager.LayoutParams lp=getActivity().getWindow().getAttributes();
                    lp.alpha=0.3f;
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getActivity().getWindow().setAttributes(lp);
                }

                break;
        }
    }
}

