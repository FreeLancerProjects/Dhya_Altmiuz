package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.adapters.ServiceAdapter;
import com.appzone.dhai.models.ServiceDataModel;
import com.appzone.dhai.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Services extends Fragment {

    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ServiceAdapter serviceAdapter;
    private ProgressBar progBar;
    private TextView tv_no_offers;
    private int current_page=1;
    private boolean isLoading = false;
    private HomeActivity activity;
    private List<ServiceDataModel.ServiceModel> serviceModelList;
    public static Fragment_Services newInstance()
    {
        return new Fragment_Services();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        serviceModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        tv_no_offers = view.findViewById(R.id.tv_no_offers);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setItemViewCacheSize(25);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        serviceAdapter = new ServiceAdapter(serviceModelList,activity,this);
        recView.setAdapter(serviceAdapter);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int lastItemPos = ((LinearLayoutManager)recView.getLayoutManager()).findLastVisibleItemPosition();
                    int itemsCount = recyclerView.getAdapter().getItemCount();
                    if ((itemsCount-lastItemPos) <= 5 && !isLoading)
                    {
                        serviceModelList.add(null);
                        serviceAdapter.notifyItemChanged(serviceModelList.size()-1);
                        isLoading = true;
                        int nextPage = current_page+1;
                        LoadMore(nextPage);
                    }
                }
            }

        });
        getServices();
    }

    private void getServices() {

        Api.getService()
                .getServices(2,1)
                .enqueue(new Callback<ServiceDataModel>() {
                    @Override
                    public void onResponse(Call<ServiceDataModel> call, Response<ServiceDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                serviceModelList.clear();
                                serviceModelList.addAll(response.body().getData());
                                if (serviceModelList.size()>0)
                                {
                                    tv_no_offers.setVisibility(View.GONE);
                                    serviceAdapter.notifyDataSetChanged();

                                }else
                                {
                                    tv_no_offers.setVisibility(View.VISIBLE);

                                }
                            }
                        }else
                        {
                            progBar.setVisibility(View.GONE);

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceDataModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    private void LoadMore(int page_index)
    {
        Api.getService()
                .getServices(2,page_index)
                .enqueue(new Callback<ServiceDataModel>() {
                    @Override
                    public void onResponse(Call<ServiceDataModel> call, Response<ServiceDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                isLoading = false;
                                serviceModelList.remove(serviceModelList.size()-1);
                                serviceModelList.addAll(response.body().getData());
                                serviceAdapter.notifyDataSetChanged();
                                current_page = response.body().getMeta().getCurrent_page();

                            }
                        }else
                        {
                            isLoading = false;
                            serviceModelList.remove(serviceModelList.size()-1);
                            serviceAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceDataModel> call, Throwable t) {
                        try {
                            isLoading = false;
                            serviceModelList.remove(serviceModelList.size()-1);
                            serviceAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
}
