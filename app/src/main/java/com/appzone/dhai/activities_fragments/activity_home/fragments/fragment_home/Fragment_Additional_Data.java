package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.remote.Api;
import com.appzone.dhai.share.Common;
import com.appzone.dhai.singletone.UserSingleTone;

import java.io.IOException;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Additional_Data extends Fragment {
    private static final String TAG ="order_id";
    private static final String TAG2="request";
    private static final String TAG3="title_ar";
    private static final String TAG4="title_en";

    private TextView tv_request;
    private EditText edt_data;
    private Button btn_send;
    private TextView tv_title;
    private UserModel userModel;
    private UserSingleTone userSingleTone;
    private HomeActivity activity;
    private int order_id;
    private String request;
    private String title_ar="",title_en="";



    public static Fragment_Additional_Data newInstance(int order_id,String request,String title_ar,String title_en)
    {
        Fragment_Additional_Data fragment_additional_data = new Fragment_Additional_Data();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG,order_id);
        bundle.putString(TAG2,request);
        bundle.putString(TAG3,title_ar);
        bundle.putString(TAG4,title_en);

        fragment_additional_data.setArguments(bundle);
        return fragment_additional_data;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_additional_details,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();

        tv_title = view.findViewById(R.id.tv_title);

        tv_request = view.findViewById(R.id.tv_request);
        edt_data = view.findViewById(R.id.edt_data);
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
            order_id = bundle.getInt(TAG,0);
            request = bundle.getString(TAG2,"");
            title_ar = bundle.getString(TAG3);
            title_en = bundle.getString(TAG4);

            updateUI(request,title_ar,title_en);
        }

    }



    private void updateUI(String request,String title_ar,String title_en) {
        tv_request.setText(request);

        if (Locale.getDefault().getLanguage().equals("ar")) {
            tv_title.setText(title_ar);
        } else {
            tv_title.setText(title_en);

        }
    }

    private void CheckData() {
        String m_data = edt_data.getText().toString().trim();

        if (!TextUtils.isEmpty(m_data))
        {
            edt_data.setError(null);
            Common.CloseKeyBoard(activity,edt_data);
            Send(m_data);
        }else
            {
                edt_data.setError(getString(R.string.field_req));
            }
    }

    private void Send(String m_data) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .sendAnswer(m_data,order_id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {

                            Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_LONG).show();
                        }else
                            {
                                dialog.dismiss();
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
}
