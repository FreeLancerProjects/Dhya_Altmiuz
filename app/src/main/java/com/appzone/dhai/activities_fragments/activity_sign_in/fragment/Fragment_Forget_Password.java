package com.appzone.dhai.activities_fragments.activity_sign_in.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_sign_in.activity.SignInActivity;
import com.appzone.dhai.remote.Api;
import com.appzone.dhai.share.Common;

import java.io.IOException;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Forget_Password extends Fragment {

    private Button btn_recover;
    private LinearLayout ll_back;
    private ImageView arrow;
    private EditText edt_email;
    private SignInActivity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Forget_Password newInstance() {
        return new Fragment_Forget_Password();
    }

    private void initView(View view) {
        activity = (SignInActivity) getActivity();
        edt_email = view.findViewById(R.id.edt_email);
        arrow = view.findViewById(R.id.arrow);
        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            arrow.setImageResource(R.drawable.arrow_right);

        }else
        {
            arrow.setImageResource(R.drawable.arrow_left);

        }
        ll_back = view.findViewById(R.id.ll_back);

        btn_recover = view.findViewById(R.id.btn_recover);




        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
    }

    private void CheckData() {
        String m_email = edt_email.getText().toString().trim();

        if (!TextUtils.isEmpty(m_email) &&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
        {
            Common.CloseKeyBoard(activity,edt_email);
            edt_email.setError(null);
            RecoverPassword(m_email);

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

            }
    }

    private void RecoverPassword(String m_email) {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .forgetPassword(m_email)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            edt_email.setText("");
                            dialog.dismiss();
                            Common.CreateSignAlertDialog(activity,getString(R.string.we_will_send_you_a_link_to_on_the_email_recover_your_password));

                            Toast.makeText(activity,R.string.succ, Toast.LENGTH_SHORT).show();
                        }else
                        {
                            dialog.dismiss();
                            if (response.code() == 422)
                            {
                                Common.CreateSignAlertDialog(activity,getString(R.string.inc_em));
                                Toast.makeText(activity, R.string.inc_code, Toast.LENGTH_SHORT).show();
                            }else
                            {
                                try {
                                    Log.e("error_code",response.code()+""+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();

                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                });
    }
}
