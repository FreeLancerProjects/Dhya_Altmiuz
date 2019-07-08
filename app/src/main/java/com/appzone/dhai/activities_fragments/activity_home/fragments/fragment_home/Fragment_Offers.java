package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home;

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
import com.appzone.dhai.adapters.OfferAdapter;
import com.appzone.dhai.models.OfferDataModel;
import com.appzone.dhai.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Offers extends Fragment {

    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private OfferAdapter offerAdapter;
    private ProgressBar progBar;
    private TextView tv_no_offers;
    private int current_page=1;
    private boolean isLoading = false;
    private HomeActivity activity;
    private List<OfferDataModel.OfferModel> offerModelList;

    public static Fragment_Offers newInstance()
    {
        return new Fragment_Offers();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        offerModelList = new ArrayList<>();
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
        offerAdapter = new OfferAdapter(offerModelList,activity);
        recView.setAdapter(offerAdapter);
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
                        offerModelList.add(null);
                        offerAdapter.notifyItemChanged(offerModelList.size()-1);
                        isLoading = true;
                        int nextPage = current_page+1;
                        LoadMore(nextPage);
                    }
                }
            }

        });
        getOffers();
    }

    private void getOffers() {

        Api.getService()
                .getOffers(1)
                .enqueue(new Callback<OfferDataModel>() {
                    @Override
                    public void onResponse(Call<OfferDataModel> call, Response<OfferDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                offerModelList.clear();
                                offerModelList.addAll(response.body().getData());
                                if (offerModelList.size()>0)
                                {
                                    tv_no_offers.setVisibility(View.GONE);
                                    offerAdapter.notifyDataSetChanged();

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
                    public void onFailure(Call<OfferDataModel> call, Throwable t) {
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
                .getOffers(page_index)
                .enqueue(new Callback<OfferDataModel>() {
                    @Override
                    public void onResponse(Call<OfferDataModel> call, Response<OfferDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                isLoading = false;
                                offerModelList.remove(offerModelList.size()-1);
                                offerModelList.addAll(response.body().getData());
                                offerAdapter.notifyDataSetChanged();
                                current_page = response.body().getMeta().getCurrent_page();

                            }
                        }else
                        {
                            isLoading = false;
                            offerModelList.remove(offerModelList.size()-1);
                            offerAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OfferDataModel> call, Throwable t) {
                        try {
                            isLoading = false;
                            offerModelList.remove(offerModelList.size()-1);
                            offerAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
}
