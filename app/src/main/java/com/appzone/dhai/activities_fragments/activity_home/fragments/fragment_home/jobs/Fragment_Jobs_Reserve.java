package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.jobs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.models.JobsDataModel;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.share.Common;
import com.appzone.dhai.singletone.UserSingleTone;

public class Fragment_Jobs_Reserve extends Fragment {
    private static final String TAG = "DATA";
    private EditText edt_name,edt_phone,edt_email,edt_id,edt_address,edt_additional_info,edt_description,edt_notes;
    private Spinner spinner;
    private String [] spinnerList;
    private Button btn_send;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private HomeActivity activity;
    private JobsDataModel.JobsModel jobsModel;
    private String qualification="";

    public static Fragment_Jobs_Reserve newInstance(JobsDataModel.JobsModel jobsModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,jobsModel);
        Fragment_Jobs_Reserve fragment_jobs_reserve = new Fragment_Jobs_Reserve();
        fragment_jobs_reserve.setArguments(bundle);
        return fragment_jobs_reserve;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        spinnerList = getResources().getStringArray(R.array.spinner_qualification);
        activity = (HomeActivity) getActivity();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();

        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_email = view.findViewById(R.id.edt_email);
        edt_id = view.findViewById(R.id.edt_id);
        edt_address = view.findViewById(R.id.edt_address);
        spinner = view.findViewById(R.id.spinner);

        edt_additional_info = view.findViewById(R.id.edt_additional_info);
        edt_description = view.findViewById(R.id.edt_description);
        edt_notes = view.findViewById(R.id.edt_notes);
        btn_send = view.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
        /////////////////////////////////////////////////////
        spinner.setAdapter(new ArrayAdapter<>(activity,R.layout.spinner_row,spinnerList));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                {
                    qualification="";
                }else
                    {
                        qualification = spinnerList[position];
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /////////////////////////////////////////////////////
        UpdateUi();

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            jobsModel = (JobsDataModel.JobsModel) bundle.getSerializable(TAG);
        }

    }

    private void UpdateUi() {
        if (userModel!=null)
        {
            edt_name.setText(userModel.getName());
            edt_phone.setText(userModel.getPhone());
            if (userModel.getEmail()!=null)
            {
                edt_email.setText(userModel.getEmail());

            }
        }
    }

    private void CheckData() {
        String m_name = edt_name.getText().toString().trim();
        String m_phone  = edt_phone.getText().toString().trim();
        String m_email = edt_email.getText().toString().trim();
        String card_id = edt_id.getText().toString().trim();
        String m_address = edt_address.getText().toString().trim();

        String m_add_info = edt_additional_info.getText().toString().trim();
        String m_description = edt_description.getText().toString().trim();
        String m_notes = edt_notes.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_phone)&&
                m_phone.length()>=6&&
                m_phone.length()<13&&
                !TextUtils.isEmpty(m_email)&&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&
                !TextUtils.isEmpty(card_id)&&
                !TextUtils.isEmpty(m_address)&&
                !TextUtils.isEmpty(qualification)
                )
        {
            Common.CloseKeyBoard(activity,edt_name);
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_email.setError(null);
            edt_id.setError(null);
            edt_address.setError(null);
            edt_additional_info.setError(null);
            edt_description.setError(null);
            edt_notes.setError(null);
            activity.jobReserveByData(jobsModel.getId(),m_name,m_phone,m_email,card_id,m_address,qualification,m_add_info,m_description,m_notes);

        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.field_req));
                }else
                    {
                        edt_name.setError(null);
                    }
                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone.setError(getString(R.string.field_req));
                }else if (m_phone.length()<6 || m_phone.length()>= 13)
                {
                    edt_phone.setError(getString(R.string.inv_phone));
                }
                else
                {
                    edt_phone.setError(null);
                }

                if (TextUtils.isEmpty(m_email))
                {
                    edt_email.setError(getString(R.string.field_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));
                }
                else
                {
                    edt_email.setError(null);
                }

                if (TextUtils.isEmpty(card_id))
                {
                    edt_id.setError(getString(R.string.field_req));
                }else
                {
                    edt_id.setError(null);
                }

                if (TextUtils.isEmpty(m_address))
                {
                    edt_address.setError(getString(R.string.field_req));
                }else
                {
                    edt_address.setError(null);
                }

                if (TextUtils.isEmpty(qualification))
                {
                    Toast.makeText(activity, R.string.ch_qualification, Toast.LENGTH_SHORT).show();
                }

            }

    }



}
