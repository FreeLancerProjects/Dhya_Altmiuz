package com.appzone.dhai.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.jobs.Fragment_Jobs;
import com.appzone.dhai.models.JobsDataModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JobsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD= 2;

    private List<JobsDataModel.JobsModel> jobsModelList;
    private Context context;
    private Fragment_Jobs fragment;

    public JobsAdapter(List<JobsDataModel.JobsModel> jobsModelList, Context context, Fragment_Jobs fragment) {
        this.jobsModelList = jobsModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.job_row,parent,false);
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
            JobsDataModel.JobsModel jobsModel = jobsModelList.get(holder.getAdapterPosition());
            MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(jobsModel);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JobsDataModel.JobsModel jobsModel = jobsModelList.get(holder.getAdapterPosition());
                    fragment.setItemData(jobsModel,holder.getAdapterPosition());
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
        return jobsModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_title,tv_details,tv_date,tv_year;

        public MyHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_details = itemView.findViewById(R.id.tv_details);

        }

        public void BindData(JobsDataModel.JobsModel jobsModel)
        {
            SimpleDateFormat dateFormat_date,dateFormat_year ;
            dateFormat_year = new SimpleDateFormat("yyyy",Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                tv_title.setText(jobsModel.getTitle_ar());
                tv_details.setText(jobsModel.getDescription_ar());
                dateFormat_date = new SimpleDateFormat("MM/dd",Locale.getDefault());

            }else
                {
                    tv_title.setText(jobsModel.getTitle_en());
                    tv_details.setText(jobsModel.getDescription_en());
                    dateFormat_date = new SimpleDateFormat("dd/MM",Locale.getDefault());

                }
            String date = dateFormat_date.format(new Date(jobsModel.getCreated_at()*1000));
            String year = dateFormat_year.format(new Date(jobsModel.getCreated_at()*1000));
            tv_year.setText(year);
            tv_date.setText(date);


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
        JobsDataModel.JobsModel jobsModel = jobsModelList.get(position);

        if (jobsModel !=null)
        {
            return ITEM_DATA;
        }else
            {
                return ITEM_LOAD;
            }
    }
}
