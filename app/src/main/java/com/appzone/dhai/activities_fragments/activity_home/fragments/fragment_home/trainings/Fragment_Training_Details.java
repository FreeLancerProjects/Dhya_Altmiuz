package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.trainings;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.adapters.PackageAdapter;
import com.appzone.dhai.models.PackageDataModel;
import com.appzone.dhai.models.TrainingDataModel;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.remote.Api;
import com.appzone.dhai.share.Common;
import com.appzone.dhai.singletone.UserSingleTone;
import com.appzone.dhai.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Training_Details extends Fragment {
    private final static String TAG = "DATA";
    private ImageView image;
    private TextView tv_name,tv_details,tv_start_time,tv_end_time,tv_cost;
    private Button btn_register;
    private TrainingDataModel.TrainingModel trainingModel;
    private HomeActivity activity;
    private String current_language;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private List<PackageDataModel.PackageModel> packageModelList;
    private PackageAdapter packageAdapter;
    private AlertDialog packageDialog,imageDialog;
    private ImageView dialog_image,image_icon;
    private Button btn_send;
    private final int IMG1=1;
    private Uri uri=null;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static Fragment_Training_Details newInstance(TrainingDataModel.TrainingModel trainingModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,trainingModel);
        Fragment_Training_Details fragment_training_details = new Fragment_Training_Details();
        fragment_training_details.setArguments(bundle);
        return fragment_training_details;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainings_details,container,false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {
        packageModelList = new ArrayList<>();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        current_language = Locale.getDefault().getLanguage();
        activity = (HomeActivity) getActivity();
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_details = view.findViewById(R.id.tv_details);
        tv_start_time = view.findViewById(R.id.tv_start_time);
        tv_end_time = view.findViewById(R.id.tv_end_time);
        tv_cost = view.findViewById(R.id.tv_cost);

        btn_register = view.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel==null)
                {
                    activity.CreateUserNotSignInAlertDialog();
                }else {

                    if (trainingModel.getSale_price()<userModel.getBalance())
                    {
                        activity.DisplayFragmentTrainingRegister();
                    }else
                        {
                            if (packageModelList.size()>0)
                            {
                                CreatePackageDialog(packageModelList);
                            }else
                                {
                                    getPackagesData();
                                }
                        }
                }
            }
        });
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            trainingModel = (TrainingDataModel.TrainingModel) bundle.getSerializable(TAG);
            UpdateUI(trainingModel);
        }

    }
    private void UpdateUI(TrainingDataModel.TrainingModel trainingModel)
    {
        if (trainingModel!=null)
        {
            Picasso.with(activity).load(Uri.parse(Tags.IMAGE_URL)+trainingModel.getImage()).fit().into(image);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MM/yyyy hh:mm aa",Locale.getDefault());
            String start_time = dateFormat.format(new Date(trainingModel.getStart()*1000));
            String end_time = dateFormat.format(new Date(trainingModel.getEnd()*1000));
            tv_start_time.setText(start_time);
            tv_end_time.setText(end_time);
            tv_cost.setText(trainingModel.getSale_price()+" "+getString(R.string.rsa));
            if (current_language.equals("ar"))
            {
                tv_name.setText(trainingModel.getDestination_name_ar());
                tv_details.setText(trainingModel.getDestination_name_ar());

            }else
                {
                    tv_name.setText(trainingModel.getDestination_name_en());
                    tv_details.setText(trainingModel.getDestination_name_en());

                }

                if (trainingModel.getUser_registered() == 0)
                {
                    btn_register.setVisibility(View.VISIBLE);
                }
        }

    }
    private void getPackagesData()
    {
        final Dialog dialog = Common.getProgressDialog(activity);
        dialog.show();
        Api.getService()
                .getPackage()
                .enqueue(new Callback<PackageDataModel>() {
                    @Override
                    public void onResponse(Call<PackageDataModel> call, Response<PackageDataModel> response) {
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            dialog.dismiss();
                            if (response.body().getDate().size()>0)
                            {
                                packageModelList.clear();
                                packageModelList.addAll(response.body().getDate());
                                CreatePackageDialog(response.body().getDate());
                            }else
                                {
                                    Toast.makeText(activity, R.string.no_charge, Toast.LENGTH_LONG).show();
                                }
                        }else
                            {

                                dialog.dismiss();
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code",response.code()+""+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                    }

                    @Override
                    public void onFailure(Call<PackageDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void CreatePackageDialog(List<PackageDataModel.PackageModel> packageModelList)
    {
         packageDialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_package,null);
        RecyclerView recView = view.findViewById(R.id.recView);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        packageAdapter = new PackageAdapter(packageModelList,activity,this);
        recView.setLayoutManager(manager);
        recView.setAdapter(packageAdapter);
        packageDialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        packageDialog.setCanceledOnTouchOutside(false);
        packageDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        packageDialog.setView(view);
        packageDialog.show();
    }
    private void CreateImageDialog(final PackageDataModel.PackageModel packageModel)
    {
        imageDialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_image_package,null);
        FrameLayout fl = view.findViewById(R.id.fl);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG1);
            }
        });
        dialog_image = view.findViewById(R.id.image);
        image_icon = view.findViewById(R.id.image_icon);
        btn_send= view.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog.dismiss();
                Send(packageModel);
            }
        });
        imageDialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        imageDialog.setCanceledOnTouchOutside(false);
        imageDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        imageDialog.setView(view);
        imageDialog.show();
    }

    private void Send(PackageDataModel.PackageModel packageModel)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        RequestBody token_part = Common.getRequestBodyText(userModel.getToken());
        RequestBody package_id = Common.getRequestBodyText(String.valueOf(packageModel.getId()));
        MultipartBody.Part image_part  = Common.getMultiPart(activity,uri,"image");

        Api.getService()
                .reservePackage(token_part,package_id,image_part)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Common.CreateSignAlertDialog(activity,getString(R.string.rev_bill));
                        }else
                        {

                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void setItemDataToBuy(PackageDataModel.PackageModel packageModel) {
        packageDialog.dismiss();
        CreateImageDialog(packageModel);
    }

    private void Check_ReadPermission(int img_req)
    {
        if (ContextCompat.checkSelfPermission(activity,read_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{read_permission},img_req);
        }else
        {
            select_photo(img_req);
        }
    }
    private void select_photo(int img1)
    {
        Intent intent ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);

        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent,img1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data!=null)
        {
            image_icon.setVisibility(View.GONE);
            uri = data.getData();

            String path = Common.getImagePath(activity,uri);
            if (path!=null)
            {
                Picasso.with(activity).load(new File(path)).fit().into(dialog_image);

            }else
            {
                Picasso.with(activity).load(uri).fit().into(dialog_image);

            }
            btn_send.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(IMG1);
                }else
                {
                    Toast.makeText(activity,getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



}
