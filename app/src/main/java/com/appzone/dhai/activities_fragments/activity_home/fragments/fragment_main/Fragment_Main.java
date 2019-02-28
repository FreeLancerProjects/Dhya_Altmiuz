package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.adapters.SliderAdapter;
import com.appzone.dhai.models.AdsDataModel;
import com.appzone.dhai.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private ViewPager pager_slider;
    private TabLayout tab_slider;
    private List<AdsDataModel.AdsModel> adsModelList;
    private SliderAdapter sliderAdapter;
    private LinearLayout ll_job,ll_training,ll_students,ll_elec_service,ll_offer,ll_other_service;
    private TextView tv_no_ads;
    private TimerTask timerTask;
    private Timer timer;



    public static Fragment_Main newInstance()
    {
        return new Fragment_Main();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        adsModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        tv_no_ads  = view.findViewById(R.id.tv_no_ads);
        pager_slider  = view.findViewById(R.id.pager_slider);
        tab_slider  = view.findViewById(R.id.tab_slider);
        ll_job  = view.findViewById(R.id.ll_job);
        ll_training= view.findViewById(R.id.ll_training);
        ll_students  = view.findViewById(R.id.ll_students);
        ll_elec_service  = view.findViewById(R.id.ll_elec_service);
        ll_offer  = view.findViewById(R.id.ll_offer);
        ll_other_service  = view.findViewById(R.id.ll_other_service);

        tab_slider.setupWithViewPager(pager_slider);


        ll_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentJobs();
            }
        });
        ll_training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentTraining();
            }
        });
        ll_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentStudents();
            }
        });

        ll_elec_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentService();
            }
        });
        ll_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentOffers();
            }
        });

        ll_other_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentOtherService();
            }
        });


        getAds();




    }

    private void getAds() {
        Api.getService()
                .getAds()
                .enqueue(new Callback<AdsDataModel>() {
                    @Override
                    public void onResponse(Call<AdsDataModel> call, Response<AdsDataModel> response) {
                        if (response.isSuccessful())
                        {

                            if (response.body()!=null)
                            {
                                adsModelList.clear();
                                adsModelList.addAll(response.body().getData());
                                UpdateUI(adsModelList);
                            }
                        }else
                        {

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AdsDataModel> call, Throwable t) {
                        try {
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void UpdateUI(List<AdsDataModel.AdsModel> adsModelList) {
        if (adsModelList.size()>0)
        {
            tv_no_ads.setVisibility(View.GONE);

            if (adsModelList.size()>1)
            {
                timerTask = new MyTimerTask();
                timer = new Timer();
                timer.scheduleAtFixedRate(timerTask,6000,6000);



            }
            sliderAdapter = new SliderAdapter(adsModelList,getActivity());
            pager_slider.setAdapter(sliderAdapter);
        }else
            {
                tv_no_ads.setVisibility(View.VISIBLE);
            }
    }


    class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager_slider.getCurrentItem()< pager_slider.getAdapter().getCount()-1)
                    {
                        pager_slider.setCurrentItem(pager_slider.getCurrentItem()+1);
                    }else
                        {
                            pager_slider.setCurrentItem(0);
                        }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (timer!=null)
        {
            timer.purge();
            timer.cancel();
        }
        if (timerTask!=null)
        {
           timerTask.cancel();
        }
    }
}
