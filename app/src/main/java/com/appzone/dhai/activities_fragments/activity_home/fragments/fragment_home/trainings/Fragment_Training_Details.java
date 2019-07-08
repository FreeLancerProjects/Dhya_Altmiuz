package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.trainings;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.models.TrainingDataModel;
import com.appzone.dhai.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Fragment_Training_Details extends Fragment {
    private final static String TAG = "DATA";
    private ImageView image;
    private TextView tv_name, tv_details,tv_organization, tv_start_time, tv_end_time, tv_cost, tv_data;
    private Button btn_register;
    private TrainingDataModel.TrainingModel trainingModel;
    private HomeActivity activity;
    private String current_language;


    public static Fragment_Training_Details newInstance(TrainingDataModel.TrainingModel trainingModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, trainingModel);
        Fragment_Training_Details fragment_training_details = new Fragment_Training_Details();
        fragment_training_details.setArguments(bundle);
        return fragment_training_details;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainings_details, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        current_language = Locale.getDefault().getLanguage();
        activity = (HomeActivity) getActivity();
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_organization = view.findViewById(R.id.tv_organization);

        tv_details = view.findViewById(R.id.tv_details);
        tv_start_time = view.findViewById(R.id.tv_start_time);
        tv_end_time = view.findViewById(R.id.tv_end_time);
        tv_cost = view.findViewById(R.id.tv_cost);
        tv_data = view.findViewById(R.id.tv_data);

        btn_register = view.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentTrainingRegister(trainingModel);

            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            trainingModel = (TrainingDataModel.TrainingModel) bundle.getSerializable(TAG);
            UpdateUI(trainingModel);
        }

    }

    private void UpdateUI(TrainingDataModel.TrainingModel trainingModel) {
        if (trainingModel != null) {
            Picasso.with(activity).load(Uri.parse(Tags.IMAGE_URL) + trainingModel.getImage()).fit().into(image);

            SimpleDateFormat dateFormat;
            tv_cost.setText(trainingModel.getSale_price() + " " + getString(R.string.rsa));
            if (current_language.equals("ar")) {
                tv_organization.setText(trainingModel.getDestination_name_ar());
                tv_name.setText(trainingModel.getTitle_ar());
                tv_details.setText(trainingModel.getDestination_name_ar());
                dateFormat = new SimpleDateFormat("EEE yyyy/MM/dd   mm:hh aa", Locale.getDefault());

            } else {
                tv_organization.setText(trainingModel.getDestination_name_en());

                tv_name.setText(trainingModel.getTitle_en());
                tv_details.setText(trainingModel.getDestination_name_en());
                dateFormat = new SimpleDateFormat("EEE dd/MM/yyyy   hh:mm aa", Locale.getDefault());

            }

            String start_time = dateFormat.format(new Date(trainingModel.getStart() * 1000));
            String end_time = dateFormat.format(new Date(trainingModel.getEnd() * 1000));
            tv_start_time.setText(start_time);
            tv_end_time.setText(end_time);

            if (trainingModel.getUser_registered() == 0) {
                btn_register.setVisibility(View.VISIBLE);
            } else {
                tv_data.setVisibility(View.VISIBLE);

            }
        }

    }


    public void UpdateUIAfterReserve() {
        btn_register.setVisibility(View.GONE);
        tv_data.setVisibility(View.VISIBLE);


    }


}
