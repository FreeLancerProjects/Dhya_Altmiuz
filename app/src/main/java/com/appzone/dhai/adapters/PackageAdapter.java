package com.appzone.dhai.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.trainings.Fragment_Training_Details;
import com.appzone.dhai.models.PackageDataModel;

import java.util.List;
import java.util.Locale;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.MyHolder> {

    private List<PackageDataModel.PackageModel> packageModelList;
    private Context context;
    private HomeActivity activity;
    public PackageAdapter(List<PackageDataModel.PackageModel> packageModelList, Context context) {
        this.packageModelList = packageModelList;
        this.context = context;
        activity = (HomeActivity) context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.package_row,parent,false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        PackageDataModel.PackageModel packageModel = packageModelList.get(position);
        holder.BindData(packageModel);
        holder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageDataModel.PackageModel packageModel = packageModelList.get(holder.getAdapterPosition());
                activity.setItemDataToBuy(packageModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return packageModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_details;
        private Button btn_buy;
        public MyHolder(View itemView) {
            super(itemView);
            tv_details = itemView.findViewById(R.id.tv_details);
            btn_buy = itemView.findViewById(R.id.btn_buy);

        }

        public void BindData(PackageDataModel.PackageModel packageModel)
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                tv_details.setText(packageModel.getTitle_ar()+"\n"+packageModel.getSize()+" "+context.getString(R.string.rsa));
            }else
                {
                    tv_details.setText(packageModel.getTitle_en()+"\n"+packageModel.getSize()+" "+context.getString(R.string.rsa));

                }


        }
    }




}
