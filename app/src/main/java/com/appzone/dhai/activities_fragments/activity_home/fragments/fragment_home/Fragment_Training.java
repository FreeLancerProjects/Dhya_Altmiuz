package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.dhai.R;

public class Fragment_Training extends Fragment {


    public static Fragment_Training newInstance()
    {
        return new Fragment_Training();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }
}
