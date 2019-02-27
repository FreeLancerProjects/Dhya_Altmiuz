package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;

public class Fragment_Notifications extends Fragment {

    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar;
    private LinearLayout ll_no_not;
    private HomeActivity activity;

    public static Fragment_Notifications newInstance()
    {
        return new Fragment_Notifications();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_no_not = view.findViewById(R.id.ll_no_not);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setItemViewCacheSize(25);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setLayoutManager(manager);

    }
}
