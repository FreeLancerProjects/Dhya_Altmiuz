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
import com.appzone.dhai.models.OfferDataModel;
import com.appzone.dhai.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD= 2;

    private List<OfferDataModel.OfferModel> offerModelList;
    private Context context;

    public OfferAdapter(List<OfferDataModel.OfferModel> offerModelList, Context context) {
        this.offerModelList = offerModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.offer_row,parent,false);
            return new MyHolder(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.load_more_row,parent,false);
                return new ProgressHolder(view);
            }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder)
        {
            OfferDataModel.OfferModel offerModel = offerModelList.get(holder.getAdapterPosition());
            MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(offerModel);
        }else
            {
                ProgressHolder progressHolder = (ProgressHolder) holder;
                progressHolder.progBar.setIndeterminate(true);
            }
    }

    @Override
    public int getItemCount() {
        return offerModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView tv_title;

        public MyHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tv_title = itemView.findViewById(R.id.tv_title);

        }

        public void BindData(OfferDataModel.OfferModel offerModel)
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                tv_title.setText(offerModel.getTitle_ar()+"\n"+offerModel.getDescription_ar());
            }else
                {
                    tv_title.setText(offerModel.getTitle_en()+"\n"+offerModel.getDescription_ar());

                }

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+offerModel.getImage())).fit().into(image);
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
        OfferDataModel.OfferModel offerModel = offerModelList.get(position);

        if (offerModel !=null)
        {
            return ITEM_DATA;
        }else
            {
                return ITEM_LOAD;
            }
    }
}
