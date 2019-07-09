package com.example.caiyue.androidstuiodemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.caiyue.androidstuiodemo.R;
import com.example.caiyue.androidstuiodemo.model.ChannelModel;

import java.util.List;

public class SelectedChannelAdapter extends BaseAdapter {
    private Context context;
    private List<ChannelModel> list;

    public SelectedChannelAdapter(Context context, List<ChannelModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.popview_channel_item, null);
            viewHolder.textView =  convertView.findViewById(R.id.channel_text);
            viewHolder.closeBtn = convertView.findViewById(R.id.icon_clear);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ChannelModel channelModel = (ChannelModel) getItem(position);
        viewHolder.textView.setText(channelModel.getDocumentClassName());
        return convertView;
    }
    class ViewHolder{
        private TextView textView;
        private LinearLayout closeBtn;
    }
}
