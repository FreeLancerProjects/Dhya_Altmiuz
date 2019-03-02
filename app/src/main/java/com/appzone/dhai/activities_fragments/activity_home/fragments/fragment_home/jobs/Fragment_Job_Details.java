package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.jobs;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.models.JobsDataModel;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.singletone.UserSingleTone;
import com.appzone.dhai.tags.Tags;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Fragment_Job_Details extends Fragment {
    private final static String TAG = "DATA";
    private ImageView image;
    private TextView tv_name,tv_details,tv_date,tv_address,tv_file_path,tv_data;
    private LinearLayout ll_register;
    private FrameLayout fl_cv;
    private Button btn_register,btn_upload_cv;
    private ExpandableLayout expand_layout;
    private JobsDataModel.JobsModel jobsModel;
    private HomeActivity activity;
    private String current_language;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private FilePickerDialog filePickerDialog;
    private String cv_path="";

    public static Fragment_Job_Details newInstance(JobsDataModel.JobsModel jobsModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,jobsModel);
        Fragment_Job_Details fragment_training_details = new Fragment_Job_Details();
        fragment_training_details.setArguments(bundle);
        return fragment_training_details;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_details,container,false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        current_language = Locale.getDefault().getLanguage();
        activity = (HomeActivity) getActivity();
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_details = view.findViewById(R.id.tv_details);
        tv_date = view.findViewById(R.id.tv_date);
        tv_address = view.findViewById(R.id.tv_address);
        tv_data = view.findViewById(R.id.tv_data);
        fl_cv = view.findViewById(R.id.fl_cv);
        ll_register = view.findViewById(R.id.ll_register);
        tv_file_path = view.findViewById(R.id.tv_file_path);
        btn_upload_cv = view.findViewById(R.id.btn_upload_cv);
        expand_layout = view.findViewById(R.id.expand_layout);

        btn_register = view.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentJobsReserve(jobsModel);

            }
        });

        fl_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expand_layout.isExpanded())
                {
                    expand_layout.collapse(true);
                }
                openPdfFile();
            }
        });

        btn_upload_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel==null)
                {
                    activity.CreateUserNotSignInAlertDialog();
                }else {

                    if (jobsModel.getSale_price()<=userModel.getBalance())
                    {
                        activity.jobReserveByPDF(jobsModel.getId(),cv_path);
                    }else
                    {
                        activity.getPackagesData();
                    }
                }
            }
        });
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            jobsModel = (JobsDataModel.JobsModel) bundle.getSerializable(TAG);
            UpdateUI(jobsModel);
        }

    }

    private void openPdfFile() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = new String[]{"pdf"};
        filePickerDialog = new FilePickerDialog(activity);
        filePickerDialog.setProperties(properties);
        filePickerDialog.setTitle("Select pdf file");
        filePickerDialog.show();

        filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                String path = files[0];
                if (path!=null)
                {
                    cv_path = path;
                    File file = new File(path);
                    tv_file_path.setText(file.getName());
                    expand_layout.expand(true);

                }

            }
        });

    }

    private void UpdateUI(JobsDataModel.JobsModel jobsModel)
    {
        if (jobsModel!=null)
        {
            Picasso.with(activity).load(Uri.parse(Tags.IMAGE_URL)+jobsModel.getImage()).fit().into(image);
            SimpleDateFormat dateFormat ;

            if (current_language.equals("ar"))
            {
                tv_name.setText(jobsModel.getTitle_ar());
                tv_details.setText(jobsModel.getDescription_ar());
                tv_address.setText(jobsModel.getAddress_ar());
                dateFormat =  new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault());

            }else
                {
                    tv_name.setText(jobsModel.getTitle_en());
                    tv_details.setText(jobsModel.getDescription_en());
                    tv_address.setText(jobsModel.getAddress_en());
                    dateFormat =  new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());


                }
            String date = dateFormat.format(new Date(jobsModel.getCreated_at()*1000));
            tv_date.setText(date);

            if (jobsModel.getUser_registered() == 0)
            {
                ll_register.setVisibility(View.VISIBLE);
            }else {
                tv_data.setVisibility(View.VISIBLE);
            }

        }

    }

    public void updateUIAfterReserve()
    {
        if (expand_layout.isExpanded())
        {
            expand_layout.collapse(true);
        }
        ll_register.setVisibility(View.GONE);
        tv_data.setVisibility(View.VISIBLE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(filePickerDialog!=null)
                    {
                        filePickerDialog.show();
                    }
                }
                else {
                    Toast.makeText(activity,"Permission is Required for getting list of files",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
