package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home;

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
import com.appzone.dhai.share.Common;

public class Fragment_Contact extends Fragment {

    private EditText edt_name, edt_email, edt_message;
    private Button btn_send;
    private HomeActivity  activity;

    public static Fragment_Contact newInstance() {
        return new Fragment_Contact();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_message = view.findViewById(R.id.edt_message);
        btn_send = view.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
    }

    private void checkData() {

        String m_name = edt_name.getText().toString().trim();
        String m_email = edt_email.getText().toString().trim();
        String m_message = edt_message.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_email) &&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches() &&
                !TextUtils.isEmpty(m_message)
                )
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_message.setError(null);
            Common.CloseKeyBoard(activity,edt_name);
            SendData(m_name,m_email,m_message);
        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.field_req));
                }else
                    {
                        edt_name.setError(null);
                    }
                if (TextUtils.isEmpty(m_email))
                {
                    edt_email.setError(getString(R.string.field_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));
                }else
                    {
                        edt_email.setError(null);
                    }


                if (TextUtils.isEmpty(m_message))
                {
                    edt_message.setError(getString(R.string.field_req));
                }else
                {
                    edt_message.setError(null);
                }
            }

    }

    private void SendData(String m_name, String m_email, String m_message) {

    }


}
