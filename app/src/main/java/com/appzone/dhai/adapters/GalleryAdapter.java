package com.appzone.dhai.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_Profile;
import com.appzone.dhai.models.BankAccountDataModel;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.tags.Tags;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyHolder> {

    private List<UserModel.User_Cvs> user_cvsList;
    private Context context;
    private Fragment_Profile fragment_profile;
    public GalleryAdapter(List<UserModel.User_Cvs> user_cvsList, Context context,Fragment_Profile fragment_profile) {
        this.user_cvsList=user_cvsList;
        this.context = context;
        this.fragment_profile=fragment_profile;
    }

    @NonNull
    @Override
    public GalleryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_gv_item,parent,false);
        return new GalleryAdapter.MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryAdapter.MyHolder holder, int position) {

        UserModel.User_Cvs user_cvs=user_cvsList.get(position);
        Picasso.with(context).load(Tags.IMAGE_URL+user_cvs.getcv_image()).fit().into(holder.ivGallery);
holder.delete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        fragment_profile.Delete(user_cvsList.get(holder.getLayoutPosition()));
    }
});
    }

    @Override
    public int getItemCount() {
        return user_cvsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivGallery;
        private ImageView delete;


        public MyHolder(View itemView) {
            super(itemView);
            ivGallery = itemView.findViewById(R.id.ivGallery);
delete=itemView.findViewById(R.id.delete_img1);
        }


    }




}