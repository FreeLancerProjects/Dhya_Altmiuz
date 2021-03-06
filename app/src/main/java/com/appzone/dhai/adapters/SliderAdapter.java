package com.appzone.dhai.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appzone.dhai.R;
import com.appzone.dhai.models.AdsDataModel;
import com.appzone.dhai.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private List<AdsDataModel.AdsModel> adsModelList;
    private Context context;

    public SliderAdapter(List<AdsDataModel.AdsModel> adsModelList, Context context) {
        this.adsModelList = adsModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return adsModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_row,container,false);
        ImageView image = view.findViewById(R.id.image);
        Picasso.with(context).load(Tags.IMAGE_URL+adsModelList.get(position).getImage()).fit().into(image);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
