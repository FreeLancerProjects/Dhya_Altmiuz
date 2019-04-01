package com.appzone.dhai.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_Notifications;
import com.appzone.dhai.models.NotificationModel;
import com.appzone.dhai.share.TimeAgo;
import com.appzone.dhai.tags.Tags;

import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD= 2;

    private List<NotificationModel> notificationModelList;
    private Context context;
    private TimeAgo timeAgo;
    private Fragment_Notifications fragment_notifications;
    public NotificationAdapter(List<NotificationModel> notificationModelList, Context context, Fragment_Notifications fragment_notifications) {
        this.notificationModelList = notificationModelList;
        this.context = context;
        this.fragment_notifications = fragment_notifications;
        timeAgo = TimeAgo.newInstance();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.notification_row,parent,false);
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
            final NotificationModel notificationModel = notificationModelList.get(holder.getAdapterPosition());
            MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(notificationModel);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationModel notificationModel = notificationModelList.get(holder.getAdapterPosition());

                    if (notificationModel.getType()==Tags.NOTIFICATION_DATA)
                    {
                        fragment_notifications.setItemData(notificationModel);
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
        return notificationModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image_state;
        private TextView tv_content,tv_date;

        public MyHolder(View itemView) {
            super(itemView);
            image_state = itemView.findViewById(R.id.image_state);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);

        }

        public void BindData(NotificationModel notificationModel)
        {

            Log.e("date",notificationModel.getCreated_at()+"__");
            tv_date.setText(timeAgo.getTime(notificationModel.getCreated_at()*1000));
            if (notificationModel.getType() == Tags.NOTIFICATION_ACCEPT_CHARGE)
            {
                tv_content.setText(context.getString(R.string.accepted)+" "+context.getString(R.string.package_1)+" "+" "+notificationModel.getService_title_ar()+" "+context.getString(R.string.rsa));
                image_state.setImageResource(R.drawable.dollar);


            }else if (notificationModel.getType()==Tags.NOTIFICATION_REFUSE_CHARGE)
            {
                tv_content.setText(context.getString(R.string.refused)+" "+context.getString(R.string.package_1)+" "+notificationModel.getService_title_ar()+" "+context.getString(R.string.rsa));
                image_state.setImageResource(R.drawable.dollar_delete);
            }else if (notificationModel.getType()==Tags.NOTIFICATION_ACCEPT_SERVICE)
            {
                image_state.setImageResource(R.drawable.done);
                if (Locale.getDefault().getLanguage().equals("ar"))
                {
                    tv_content.setText(context.getString(R.string.accepted)+" "+notificationModel.getService_title_ar());

                }else
                {
                    tv_content.setText(context.getString(R.string.accepted)+" "+notificationModel.getService_title_en());

                }
            }
            else if (notificationModel.getType()==Tags.NOTIFICATION_REFUSE_SERVICE)
            {
                image_state.setImageResource(R.drawable.close_red_color);
                if (Locale.getDefault().getLanguage().equals("ar"))
                {
                    tv_content.setText(context.getString(R.string.refused)+" "+notificationModel.getService_title_ar());

                }else
                {
                    tv_content.setText(context.getString(R.string.refused)+" "+notificationModel.getService_title_en());

                }
            }
            else if (notificationModel.getType()==Tags.NOTIFICATION_FINISH_SERVICE)
            {
                image_state.setImageResource(R.drawable.done);
                if (Locale.getDefault().getLanguage().equals("ar"))
                {
                    tv_content.setText(context.getString(R.string.finished)+" "+notificationModel.getService_title_ar());

                }else
                {
                    tv_content.setText(context.getString(R.string.finished)+" "+notificationModel.getService_title_en());

                }
            }else if (notificationModel.getType()==Tags.NOTIFICATION_DATA)
            {
                image_state.setImageResource(R.drawable.add_info);

                if (Locale.getDefault().getLanguage().equals("ar"))
                {
                    tv_content.setText(notificationModel.getService_title_ar()+" "+context.getString(R.string.add_info_req));

                }else
                {
                    tv_content.setText(notificationModel.getService_title_en()+" "+context.getString(R.string.add_info_req));

                }
            }

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
        NotificationModel notificationModel = notificationModelList.get(position);

        if (notificationModel !=null)
        {
            return ITEM_DATA;
        }else
            {
                return ITEM_LOAD;
            }
    }
}
