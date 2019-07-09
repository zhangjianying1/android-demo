package com.example.caiyue.androidstuiodemo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.caiyue.androidstuiodemo.R;
import com.example.caiyue.androidstuiodemo.activity.MapActivity;

public class IntelligentFragment extends Fragment {
    private ImageButton imageButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intelligent, null);
        initView(view);
        return view;
    }
    private void initView(View view){
        imageButton = view.findViewById(R.id.map);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), MapActivity.class);
                startActivity(it);
            }
        });
    }
}
