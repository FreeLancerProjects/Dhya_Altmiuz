package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.trainings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.models.TrainingDataModel;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.share.Common;
import com.appzone.dhai.singletone.UserSingleTone;

public class Fragment_Training_Register extends Fragment {
    private static final String TAG="DATA";
    private EditText edt_name,edt_phone,edt_email,edt_additional_info,edt_description,edt_notes;
    private Button btn_send;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private HomeActivity activity;
    private TrainingDataModel.TrainingModel trainingModel;

    public static Fragment_Training_Register newInstance(TrainingDataModel.TrainingModel trainingModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,trainingModel);
        Fragment_Training_Register fragment_training_register = new Fragment_Training_Register();
        fragment_training_register.setArguments(bundle);

        return fragment_training_register;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();

        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_email = view.findViewById(R.id.edt_email);
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
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            trainingModel = (TrainingDataModel.TrainingModel) bundle.getSerializable(TAG);
        }
        UpdateUi();


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
        String m_add_info = edt_additional_info.getText().toString().trim();
        String m_description = edt_description.getText().toString().trim();
        String m_notes = edt_notes.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_phone)&&
                m_phone.length()==9&&
                !TextUtils.isEmpty(m_email)&&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&
                !TextUtils.isEmpty(m_add_info)
                )
        {
            Common.CloseKeyBoard(activity,edt_name);
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_email.setError(null);
            edt_additional_info.setError(null);
            edt_description.setError(null);
            edt_notes.setError(null);
            edt_additional_info.setError(null);
            activity.trainingReserve(trainingModel.getId(),m_name,m_phone,m_email,m_add_info,m_description,m_notes);

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
                }else if (m_phone.length()!=9)
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

                if (TextUtils.isEmpty(m_add_info))
                {
                    edt_additional_info.setError(getString(R.string.field_req));
                }else
                {
                    edt_additional_info.setError(null);
                }


            }

    }



}
