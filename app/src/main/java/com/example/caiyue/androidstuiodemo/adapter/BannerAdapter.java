package com.example.caiyue.androidstuiodemo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private List<View> viewList;
    public BannerAdapter(List<View> viewList){
        this.viewList = viewList;
    }
    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int newPosition = position % viewList.size();
        View view = viewList.get(newPosition);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int newPosition = position % viewList.size();
        container.removeView(viewList.get(newPosition));
    }
}
