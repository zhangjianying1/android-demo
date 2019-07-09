package com.example.caiyue.androidstuiodemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.caiyue.androidstuiodemo.model.ChannelModel;

import java.util.List;

public class SubscribeChannelAdapter extends BaseAdapter {
    private Context context;
    private List<ChannelModel> list;

    public SubscribeChannelAdapter(Context context, List<ChannelModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
