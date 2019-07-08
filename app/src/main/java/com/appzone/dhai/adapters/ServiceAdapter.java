package com.appzone.dhai.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.services.Fragment_Other_Services;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.services.Fragment_Services;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.services.Fragment_Student;
import com.appzone.dhai.models.ServiceDataModel;

import java.util.List;
import java.util.Locale;

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD= 2;

    private List<ServiceDataModel.ServiceModel> serviceModelList;
    private Context context;
    private Fragment fragment;

    public ServiceAdapter(List<ServiceDataModel.ServiceModel> serviceModelList, Context context,Fragment fragment) {
        this.serviceModelList = serviceModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.service_row,parent,false);
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
            ServiceDataModel.ServiceModel serviceModel = serviceModelList.get(holder.getAdapterPosition());
            MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(serviceModel);
            myHolder.btn_reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ServiceDataModel.ServiceModel serviceModel = serviceModelList.get(holder.getAdapterPosition());
                    if (fragment instanceof Fragment_Student)
                    {
                        Fragment_Student fragment_student = (Fragment_Student) fragment;
                        fragment_student.setItemData(serviceModel);
                    }else if (fragment instanceof Fragment_Other_Services)
                    {
                        Fragment_Other_Services fragment_other_services = (Fragment_Other_Services) fragment;
                        fragment_other_services.setItemData(serviceModel);
                    }else if (fragment instanceof Fragment_Services)
                    {
                        Fragment_Services fragment_services = (Fragment_Services) fragment;
                        fragment_services.setItemData(serviceModel);

                    }
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
        return serviceModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_name,tv_price;
        private Button btn_reserve;

        public MyHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);

            btn_reserve = itemView.findViewById(R.id.btn_reserve);

        }

        public void BindData(ServiceDataModel.ServiceModel serviceModel)
        {
            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                tv_name.setText(serviceModel.getTitle_ar());
            }else
                {
                    tv_name.setText(serviceModel.getTitle_en());

                }

                tv_price.setText(serviceModel.getSale_price()+" "+context.getString(R.string.rsa));

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
        ServiceDataModel.ServiceModel serviceModel = serviceModelList.get(position);

        if (serviceModel !=null)
        {
            return ITEM_DATA;
        }else
            {
                return ITEM_LOAD;
            }
    }
}
