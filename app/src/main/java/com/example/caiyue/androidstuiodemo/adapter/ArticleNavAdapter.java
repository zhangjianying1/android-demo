package com.example.caiyue.androidstuiodemo.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.caiyue.androidstuiodemo.R;
import com.example.caiyue.androidstuiodemo.model.ChannelModel;

import java.util.List;

public class ArticleNavAdapter extends RecyclerView.Adapter<ArticleNavAdapter.ViewHolder>{
    private List<ChannelModel> listChannel;
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public ArticleNavAdapter(List<ChannelModel> data) {
        this.listChannel = data;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public ArticleNavAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_nav_item, parent, false);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ChannelModel channelModel = listChannel.get(position);
        if (channelModel.getIsSelected() == true) {
            holder.textView.setTextColor(Color.parseColor("#ff9900"));
        } else {
            holder.textView.setTextColor(Color.parseColor("#dddddd"));
        }
        Log.i("art", channelModel.toString());
        holder.textView.setText(channelModel.getDocumentClassName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerViewItemClickListener.onItemClick(holder.textView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listChannel.size();
    }
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
        this.onRecyclerViewItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.article_nav_item);
        }
    }
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view ,int position);
    }
}
