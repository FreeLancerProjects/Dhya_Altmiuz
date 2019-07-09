package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.adapters.GalleryAdapter;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.preferences.Preferences;
import com.appzone.dhai.remote.Api;
import com.appzone.dhai.share.Common;
import com.appzone.dhai.singletone.UserSingleTone;
import com.appzone.dhai.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment {

    private AppBarLayout appBar;
    private CircleImageView image;
    private TextView tv_name, tv_email, tv_balance;
    private ImageView arrow1, arrow2, arrow3, arrow4;
    private LinearLayout ll_name, ll_email, ll_logout, ll_charge, ll_cv;
    private RecyclerView recyclerView_cvimages;
    private String current_language;
    private HomeActivity activity;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private final int IMG1 = 1, IMG2 = 2;
    private Uri uri = null;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private GalleryAdapter galleryAdapter;
    private List<Uri> uriList;
    private List<UserModel.User_Cvs> user_cvsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }

    private void initView(View view) {
        user_cvsList= new ArrayList<>();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        current_language = Locale.getDefault().getLanguage();
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);

        if (current_language.equals("ar")) {
            arrow1.setImageResource(R.drawable.black_left_arrow);
            arrow2.setImageResource(R.drawable.black_left_arrow);
            arrow3.setImageResource(R.drawable.black_left_arrow);
            arrow4.setImageResource(R.drawable.black_left_arrow);


        } else {
            arrow1.setImageResource(R.drawable.black_right_arrow);
            arrow2.setImageResource(R.drawable.black_right_arrow);
            arrow3.setImageResource(R.drawable.black_right_arrow);
            arrow4.setImageResource(R.drawable.black_right_arrow);

        }

        activity = (HomeActivity) getActivity();
        appBar = view.findViewById(R.id.appBar);
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_balance = view.findViewById(R.id.tv_balance);
        tv_email = view.findViewById(R.id.tv_email);
        ll_name = view.findViewById(R.id.ll_name);
        ll_email = view.findViewById(R.id.ll_email);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_charge = view.findViewById(R.id.ll_charge);
        ll_cv = view.findViewById(R.id.ll_cv);
        recyclerView_cvimages = view.findViewById(R.id.recView_images);
        recyclerView_cvimages.setDrawingCacheEnabled(true);
        recyclerView_cvimages.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recyclerView_cvimages.setItemViewCacheSize(25);
        galleryAdapter = new GalleryAdapter(user_cvsList, activity,this);
        recyclerView_cvimages.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,true));
        recyclerView_cvimages.setAdapter(galleryAdapter);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int range = appBarLayout.getTotalScrollRange();

                if ((range + verticalOffset) <= 150) {
                    image.setVisibility(View.GONE);
                } else {
                    image.setVisibility(View.VISIBLE);

                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG1);
            }
        });
        ll_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG2);
            }
        });

        ll_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialogUpdateName(userModel.getName());
            }
        });
        ll_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialogUpdateEmail(userModel.getEmail());
            }
        });

        ll_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChargeAlertDialog();
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Logout();
            }
        });
        updateUI(userModel);

    }

    private void updateUI(UserModel userModel) {

        if (userModel != null) {

            if (userModel.getAvatar() != null) {
                Picasso.with(activity).load(Tags.IMAGE_URL + userModel.getAvatar()).fit().into(image);

            } else {
                image.setImageResource(R.drawable.user_profile);
            }

            if (userModel.getUser_cvs() != null) {

                user_cvsList.clear();
                user_cvsList.addAll(userModel.getUser_cvs());
                galleryAdapter.notifyDataSetChanged();
            }

            tv_name.setText(userModel.getName());
            tv_email.setText(userModel.getEmail());
            tv_balance.setText(String.format("%.2f", userModel.getBalance()) + " " + getString(R.string.rsa));
        }

    }

    private void Check_ReadPermission(int img_req) {
        if (ContextCompat.checkSelfPermission(activity, read_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{read_permission}, img_req);
        } else {
            select_photo(img_req);
        }
    }

    private void select_photo(int img1) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            if (img1 == 2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (img1 == 2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }

        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, img1);
    }

    private void UpdateImage(Uri uri) {

        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();

        RequestBody token_part = Common.getRequestBodyText(userModel.getToken());


        try {
            MultipartBody.Part avatar_part = Common.getMultiPart(activity, uri, "avatar");
            Api.getService()
                    .updateImage(token_part, avatar_part)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                            if (response.isSuccessful()) {
                                dialog.dismiss();

                                if (response.body() != null) {
                                    Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                    UpdateUserData(response.body());


                                } else {
                                    Common.CreateSignAlertDialog(activity, getString(R.string.something));
                                }
                            } else {

                                dialog.dismiss();

                                if (response.code() == 422) {
                                    Common.CreateSignAlertDialog(activity, getString(R.string.phone_number_exists));

                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }

                                try {
                                    Log.e("error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(activity, R.string.inc_img_path, Toast.LENGTH_SHORT).show();

        }

    }

    private void CreateDialogUpdateName(String old_name) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_update_name, null);
        final EditText edt_name = view.findViewById(R.id.edt_name);
        edt_name.setText(old_name);
        Button btn_update = view.findViewById(R.id.btn_update);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    alertDialog.dismiss();
                    edt_name.setError(null);
                    Common.CloseKeyBoard(activity, edt_name);
                    updateName(name);
                } else {
                    edt_name.setError(getString(R.string.field_req));
                }
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void CreateDialogUpdateEmail(String old_email) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_update_email, null);
        final EditText edt_email = view.findViewById(R.id.edt_email);
        edt_email.setText(old_email);
        Button btn_update = view.findViewById(R.id.btn_update);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    alertDialog.dismiss();
                    edt_email.setError(null);
                    Common.CloseKeyBoard(activity, edt_email);
                    updateEmail(email);
                } else if (TextUtils.isEmpty(email)) {
                    edt_email.setError(getString(R.string.field_req));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edt_email.setError(getString(R.string.inv_email));
                }
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void updateName(String name) {

        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();

        Api.getService()
                .updateName(userModel.getToken(), name)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                        if (response.isSuccessful()) {
                            dialog.dismiss();

                            if (response.body() != null) {
                                Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                UpdateUserData(response.body());


                            } else {
                                Common.CreateSignAlertDialog(activity, getString(R.string.something));
                            }
                        } else {

                            dialog.dismiss();

                            if (response.code() == 422) {
                                Common.CreateSignAlertDialog(activity, getString(R.string.phone_number_exists));

                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });


    }

    private void updateEmail(String email) {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();

        Api.getService()
                .updateEmail(userModel.getToken(), email)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                        if (response.isSuccessful()) {
                            dialog.dismiss();

                            if (response.body() != null) {
                                Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                UpdateUserData(response.body());


                            } else {
                                Common.CreateSignAlertDialog(activity, getString(R.string.something));
                            }
                        } else {

                            dialog.dismiss();

                            if (response.code() == 422) {
                                Common.CreateSignAlertDialog(activity, getString(R.string.phone_number_exists));

                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });


    }

    private void UpdateUserData(UserModel userModel) {
        this.userModel = userModel;
        userSingleTone.setUserModel(userModel);
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_userData(activity, userModel);
        updateUI(userModel);
        activity.updateUserData(userModel);
    }

    private void uploadCV() {

        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();

        RequestBody token_part = Common.getRequestBodyText(userModel.getToken());


        try {
            List<MultipartBody.Part> partImageList = getMultipartBodyList(uriList, "image_cv[]");

            Api.getService()
                    .updateCv(token_part, partImageList)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                            if (response.isSuccessful()) {
                                dialog.dismiss();

                                if (response.body() != null) {
                                    Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                    UpdateUserData(response.body());


                                } else {
                                    Common.CreateSignAlertDialog(activity, getString(R.string.something));
                                }
                            } else {

                                dialog.dismiss();

                                if (response.code() == 422) {
                                    Common.CreateSignAlertDialog(activity, getString(R.string.phone_number_exists));

                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }

                                try {
                                    Log.e("error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(activity, R.string.inc_img_path, Toast.LENGTH_SHORT).show();

        }
    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(activity,uri,image_cv);
            partList.add(part);
        }
        return partList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();

            UpdateImage(uri);
        } else if (requestCode == IMG2 && resultCode == Activity.RESULT_OK && data != null) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (data.getData() != null) {

                Uri mImageUri = data.getData();

                // Get the cursor
                Cursor cursor = activity.getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();


                cursor.close();

                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                mArrayUri.add(mImageUri);

                uriList = mArrayUri;
                uploadCV();
            } else {

                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        // Get the cursor
                        Cursor cursor = activity.getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();


                        cursor.close();
                    }
                    uriList = mArrayUri;


                    uploadCV();
                    Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                }

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(IMG1);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == IMG2) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(IMG2);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void requestNewProfile() {
        Api.getService()
                .getProfileData(userModel.getToken())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            UpdateUserData(response.body());
                        } else {
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }


    private void CreateChargeAlertDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_charge, null);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        final EditText edt_code = view.findViewById(R.id.edt_code);
        ImageView image_close = view.findViewById(R.id.image_close);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String code = edt_code.getText().toString().trim();

                if (!TextUtils.isEmpty(code)) {

                    edt_code.setError(null);
                    Common.CloseKeyBoard(activity, edt_code);
                    Charge(code, dialog);
                } else {
                    edt_code.setError(getString(R.string.field_req));

                }
            }
        });

        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void Charge(final String code, final AlertDialog alertDialog) {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();
        Api.getService()
                .chargeCoupon(code, userModel.getToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            dialog.dismiss();
                            alertDialog.dismiss();
                            Toast.makeText(activity, R.string.succ, Toast.LENGTH_SHORT).show();
                            requestNewProfile();
                        } else {
                            dialog.dismiss();
                            if (response.code() == 422) {
                                Toast.makeText(activity, R.string.inc_code, Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    Log.e("error_code", response.code() + "" + response.errorBody().string());
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
                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void Delete(UserModel.User_Cvs user_cvs) {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();



        try {

            Api.getService()
                    .deltecv(userModel.getToken(), user_cvs.getId())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                            if (response.isSuccessful()) {
                                dialog.dismiss();

                                if (response.body() != null) {
                                    Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                    UpdateUserData(response.body());


                                } else {
                                    Common.CreateSignAlertDialog(activity, getString(R.string.something));
                                }
                            } else {

                                dialog.dismiss();

                                if (response.code() == 422) {
                                    Common.CreateSignAlertDialog(activity, getString(R.string.phone_number_exists));

                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }

                                try {
                                    Log.e("error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(activity, R.string.inc_img_path, Toast.LENGTH_SHORT).show();

        }
    }
}
