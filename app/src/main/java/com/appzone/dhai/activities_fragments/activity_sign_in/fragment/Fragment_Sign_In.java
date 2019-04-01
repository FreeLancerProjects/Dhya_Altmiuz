package com.appzone.dhai.activities_fragments.activity_sign_in.fragment;

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
import com.appzone.dhai.activities_fragments.activity_sign_in.activity.SignInActivity;
import com.appzone.dhai.share.Common;


public class Fragment_Sign_In extends Fragment {
    private Button btn_skip, btn_terms, btn_sign_in, btn_sign_up,btn_forget_password;
    private EditText edt_email, edt_password;
    private SignInActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }

    private void initView(View view) {
        activity = (SignInActivity) getActivity();
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);

        btn_forget_password = view.findViewById(R.id.btn_forget_password);

        btn_skip = view.findViewById(R.id.btn_skip);
        btn_terms = view.findViewById(R.id.btn_terms);
        btn_sign_in = view.findViewById(R.id.btn_sign_in);
        btn_sign_up = view.findViewById(R.id.btn_sign_up);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentCompleteProfile();
            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToHomeActivity(true, false);
            }
        });

        btn_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToTermsActivity();
            }
        });

        btn_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentForgetPassword();
            }
        });
    }

    private void CheckData() {
        String m_email = edt_email.getText().toString().trim();
        String m_password = edt_password.getText().toString();

        if (!TextUtils.isEmpty(m_email) &&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&
                !TextUtils.isEmpty(m_password))
        {
            Common.CloseKeyBoard(activity,edt_email);
            edt_email.setError(null);
            edt_password.setError(null);
            activity.signIn(m_email,m_password);
        } else
            {
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

                if (TextUtils.isEmpty(m_password))
                {
                    edt_password.setError(getString(R.string.field_req));

                }
                else
                {
                    edt_password.setError(null);

                }
            }
    }
}
