package com.appzone.dhai.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.trainings.Fragment_Training;
import com.appzone.dhai.models.TrainingDataModel;
import com.appzone.dhai.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrainingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD= 2;

    private List<TrainingDataModel.TrainingModel> trainingModelList;
    private Context context;
    private Fragment_Training fragment;

    public TrainingAdapter(List<TrainingDataModel.TrainingModel> trainingModelList, Context context, Fragment_Training fragment) {
        this.trainingModelList = trainingModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.training_row,parent,false);
            return new MyHolder(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.load_more_row,parent,false);
                return new ProgressHolder(view);
            }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder)
        {
            TrainingDataModel.TrainingModel trainingModel = trainingModelList.get(holder.getAdapterPosition());
            MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(trainingModel);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TrainingDataModel.TrainingModel trainingModel = trainingModelList.get(holder.getAdapterPosition());

                    fragment.setItemData(trainingModel);
                }
            });

        }else
            {
                ProgressHolder progressHolder = (ProgressHolder) holder;
                progressHolder.progBar.setIndeterminate(true);
            }
    }

    @Override
    public int getItemCount() {
        return trainingModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView tv_name,tv_description,tv_date;
        private AVLoadingIndicatorView avLoadingIndicator;

        public MyHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_description = itemView.findViewById(R.id.tv_description);
            avLoadingIndicator = itemView.findViewById(R.id.avLoadingIndicator);
            avLoadingIndicator.smoothToShow();
        }

        public void BindData(TrainingDataModel.TrainingModel trainingModel)
        {
            SimpleDateFormat dateFormat ;

            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                tv_name.setText(trainingModel.getDestination_name_ar());
                tv_description.setText(trainingModel.getDescription_ar());
                dateFormat  = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault());
            }else
                {
                    tv_name.setText(trainingModel.getDestination_name_en());
                    tv_description.setText(trainingModel.getDescription_en());
                    dateFormat= new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());

                }
            String date = dateFormat.format(new Date(trainingModel.getCreated_at()*1000));
            tv_date.setText(date);
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+trainingModel.getImage())).fit().into(image, new Callback() {
                @Override
                public void onSuccess() {
                    avLoadingIndicator.smoothToHide();
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    public class ProgressHolder extends RecyclerView.ViewHolder {
        private ProgressBar progBar;
        public ProgressHolder(View itemView) {
            super(itemView);
            progBar = itemView.findViewById(R.id.progBar);

        }
    }

    @Override
    public int getItemViewType(int position) {
        TrainingDataModel.TrainingModel trainingModel = trainingModelList.get(position);

        if (trainingModel !=null)
        {
            return ITEM_DATA;
        }else
            {
                return ITEM_LOAD;
            }
    }
}
