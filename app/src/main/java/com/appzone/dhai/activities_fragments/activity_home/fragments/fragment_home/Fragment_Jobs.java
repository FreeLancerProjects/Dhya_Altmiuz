package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.dhai.R;

public class Fragment_Jobs extends Fragment {


    public static Fragment_Jobs newInstance()
    {
        return new Fragment_Jobs();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }
}
