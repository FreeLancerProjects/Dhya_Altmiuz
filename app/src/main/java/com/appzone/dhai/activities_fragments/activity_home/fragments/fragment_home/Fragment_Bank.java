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
import com.appzone.dhai.adapters.BankAdapter;
import com.appzone.dhai.models.BankAccountDataModel;
import com.appzone.dhai.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Bank extends Fragment {

    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar;
    private TextView tv_no_account;
    private BankAdapter adapter;
    private List<BankAccountDataModel.BankModel> bankModelList;
    private HomeActivity activity;

    public static Fragment_Bank newInstance()
    {
        return new Fragment_Bank();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        bankModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        tv_no_account = view.findViewById(R.id.tv_no_account);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new BankAdapter(bankModelList,activity);
        recView.setAdapter(adapter);

        getBanks();
    }

    private void getBanks() {
        Api.getService()
                .getBankAccount()
                .enqueue(new Callback<BankAccountDataModel>() {
                    @Override
                    public void onResponse(Call<BankAccountDataModel> call, Response<BankAccountDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                bankModelList.clear();
                                bankModelList.addAll(response.body().getData());
                                if (bankModelList.size()>0)
                                {
                                    tv_no_account.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();

                                }else
                                {
                                    tv_no_account.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<BankAccountDataModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
}
