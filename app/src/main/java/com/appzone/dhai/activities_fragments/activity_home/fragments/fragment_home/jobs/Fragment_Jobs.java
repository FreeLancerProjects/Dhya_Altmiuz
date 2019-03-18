package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.jobs;

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
import com.appzone.dhai.adapters.JobsAdapter;
import com.appzone.dhai.models.JobsDataModel;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.remote.Api;
import com.appzone.dhai.singletone.UserSingleTone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Jobs extends Fragment {

    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private JobsAdapter jobsAdapter;
    private ProgressBar progBar;
    private TextView tv_no_jobs;
    private int current_page=1;
    private boolean isLoading = false;
    private HomeActivity activity;
    private List<JobsDataModel.JobsModel> jobsModelList;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private String user_token="";
    private int selectedPos = -1;
    private JobsDataModel.JobsModel selectedJobModel=null;
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

        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        if (userModel!=null)
        {
            user_token = userModel.getToken();
        }
        jobsModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        tv_no_jobs = view.findViewById(R.id.tv_no_jobs);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setItemViewCacheSize(25);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        jobsAdapter = new JobsAdapter(jobsModelList,activity,this);
        recView.setAdapter(jobsAdapter);
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
                        jobsModelList.add(null);
                        jobsAdapter.notifyItemChanged(jobsModelList.size()-1);
                        isLoading = true;
                        int nextPage = current_page+1;
                        LoadMore(nextPage);
                    }
                }
            }

        });
        getJobs();
    }

    public void getJobs() {

        Api.getService()
                .getJobs(user_token,1)
                .enqueue(new Callback<JobsDataModel>() {
                    @Override
                    public void onResponse(Call<JobsDataModel> call, Response<JobsDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                jobsModelList.clear();
                                jobsModelList.addAll(response.body().getData());
                                if (jobsModelList.size()>0)
                                {
                                    tv_no_jobs.setVisibility(View.GONE);
                                    jobsAdapter.notifyDataSetChanged();

                                }else
                                {
                                    tv_no_jobs.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<JobsDataModel> call, Throwable t) {
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
                .getJobs(user_token,page_index)
                .enqueue(new Callback<JobsDataModel>() {
                    @Override
                    public void onResponse(Call<JobsDataModel> call, Response<JobsDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                isLoading = false;
                                jobsModelList.remove(jobsModelList.size()-1);
                                jobsModelList.addAll(response.body().getData());
                                jobsAdapter.notifyDataSetChanged();
                                current_page = response.body().getMeta().getCurrent_page();

                            }
                        }else
                        {
                            isLoading = false;
                            jobsModelList.remove(jobsModelList.size()-1);
                            jobsAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JobsDataModel> call, Throwable t) {
                        try {
                            isLoading = false;
                            jobsModelList.remove(jobsModelList.size()-1);
                            jobsAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void RefreshAdapter()
    {
        if (selectedPos!=-1 && selectedJobModel!=null)
        {

            selectedJobModel.setUser_registered(1);
            jobsModelList.set(selectedPos,selectedJobModel);
            jobsAdapter.notifyItemChanged(selectedPos,selectedJobModel);
            selectedPos = -1;
            selectedJobModel = null;
        }
    }
    public void setItemData(JobsDataModel.JobsModel jobsModel, int pos) {

        activity.DisplayFragmentJobsDetails(jobsModel);
        selectedJobModel = jobsModel;
        selectedPos = pos;

    }
}
