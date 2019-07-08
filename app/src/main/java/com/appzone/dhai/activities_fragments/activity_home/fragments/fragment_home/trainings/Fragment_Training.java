package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.trainings;

import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.adapters.TrainingAdapter;
import com.appzone.dhai.models.TrainingDataModel;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.remote.Api;
import com.appzone.dhai.singletone.UserSingleTone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Training extends Fragment {

    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private TrainingAdapter trainingAdapter;
    private ProgressBar progBar;
    private TextView tv_no_training;
    private int current_page=1;
    private boolean isLoading = false;
    private HomeActivity activity;
    private List<TrainingDataModel.TrainingModel> trainingModelList;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private String user_token="";
    public static Fragment_Training newInstance()
    {
        return new Fragment_Training();
    }
    private int selectedPos = -1;
    private TrainingDataModel.TrainingModel selectedTrainingModel=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training,container,false);
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
        trainingModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        tv_no_training = view.findViewById(R.id.tv_no_training);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setItemViewCacheSize(25);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        trainingAdapter = new TrainingAdapter(trainingModelList,activity,this);
        recView.setAdapter(trainingAdapter);
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
                        trainingModelList.add(null);
                        trainingAdapter.notifyItemChanged(trainingModelList.size()-1);
                        isLoading = true;
                        int nextPage = current_page+1;
                        LoadMore(nextPage);
                    }
                }
            }

        });
        getTrainings();
    }

    public void getTrainings() {

        Api.getService()
                .getTrainings(user_token,1)
                .enqueue(new Callback<TrainingDataModel>() {
                    @Override
                    public void onResponse(Call<TrainingDataModel> call, Response<TrainingDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                trainingModelList.clear();
                                trainingModelList.addAll(response.body().getData());
                                if (trainingModelList.size()>0)
                                {
                                    tv_no_training.setVisibility(View.GONE);
                                    trainingAdapter.notifyDataSetChanged();

                                }else
                                {
                                    tv_no_training.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<TrainingDataModel> call, Throwable t) {
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
                .getTrainings(user_token,page_index)
                .enqueue(new Callback<TrainingDataModel>() {
                    @Override
                    public void onResponse(Call<TrainingDataModel> call, Response<TrainingDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                isLoading = false;
                                trainingModelList.remove(trainingModelList.size()-1);
                                trainingModelList.addAll(response.body().getData());
                                trainingAdapter.notifyDataSetChanged();
                                current_page = response.body().getMeta().getCurrent_page();

                            }
                        }else
                        {
                            isLoading = false;
                            trainingModelList.remove(trainingModelList.size()-1);
                            trainingAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TrainingDataModel> call, Throwable t) {
                        try {
                            isLoading = false;
                            trainingModelList.remove(trainingModelList.size()-1);
                            trainingAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void RefreshAdapter()
    {
        if (selectedPos!=-1 && selectedTrainingModel!=null)
        {

            selectedTrainingModel.setUser_registered(1);
            trainingModelList.set(selectedPos,selectedTrainingModel);
            trainingAdapter.notifyItemChanged(selectedPos,selectedTrainingModel);
            selectedPos = -1;
            selectedTrainingModel = null;
        }
    }

    public void setItemData(TrainingDataModel.TrainingModel trainingModel, int pos) {
        activity.DisplayFragmentTrainingDetails(trainingModel);
        selectedPos = pos;
        selectedTrainingModel = trainingModel;
    }
}
