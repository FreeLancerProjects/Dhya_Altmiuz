package com.appzone.dhai.activities_fragments.activity_sign_in.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.dhai.activities_fragments.activity_sign_in.fragment.Fragment_Sign_In;
import com.appzone.dhai.activities_fragments.activity_sign_in.fragment.Fragment_Sign_Up;
import com.appzone.dhai.activities_fragments.activity_sign_in.fragment.Fragment_Phone;
import com.appzone.dhai.activities_fragments.activity_terms_conditions.TermsConditionsActivity;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.preferences.Preferences;
import com.appzone.dhai.remote.Api;
import com.appzone.dhai.share.Common;
import com.appzone.dhai.singletone.UserSingleTone;

import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private View root;
    private Snackbar snackbar;
    private FragmentManager fragmentManager;
    private Fragment_Sign_In fragment_signIn;
    private Fragment_Phone fragment_phone;
    private Fragment_Sign_Up fragment_signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView() {


        fragmentManager = getSupportFragmentManager();
        root = findViewById(R.id.root);
        DisplayFragmentSignIn();




    }

    public void DisplayFragmentSignIn()
    {
        if (fragment_signUp !=null&& fragment_signUp.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_signUp).commit();
        }

        if (fragment_signIn ==null)
        {
            fragment_signIn = Fragment_Sign_In.newInstance();
        }

        if (fragment_signIn.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_signIn).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_container, fragment_signIn,"fragment_sign_in").addToBackStack("fragment_sign_in").commit();
        }

    }

    public void DisplayFragmentPhone()
    {


        if (fragment_phone==null)
        {
            fragment_phone = Fragment_Phone.newInstance();
        }

        if (fragment_phone.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_phone).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_container,fragment_phone,"fragment_phone").addToBackStack("fragment_phone").commit();
        }

    }

    public void DisplayFragmentCompleteProfile()
    {

        if (fragment_signIn!=null&&fragment_signIn.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_signIn).commit();
        }
        if (fragment_signUp ==null)
        {
            fragment_signUp = Fragment_Sign_Up.newInstance();
        }

        if (fragment_signUp.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_signUp).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_container, fragment_signUp,"fragment_signUp").addToBackStack("fragment_signUp").commit();
        }

    }
    public void NavigateToHomeActivity(boolean isSkip,boolean isSignUp)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        if (isSignUp)
        {
            intent.putExtra("signup",1);
        }
        startActivity(intent);

        if (!isSkip)
        {
            finish();

        }


    }

    public void NavigateToTermsActivity()
    {
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        startActivity(intent);
    }

    public void signIn(String email,String password)
    {
        final Dialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .SignIn(email,password)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            DismissSnackBar();
                            dialog.dismiss();

                            if (response.body()!=null)
                            {
                                UserSingleTone userSingleTone = UserSingleTone.getInstance();
                                Preferences preferences = Preferences.getInstance();

                                UserModel userModel = response.body();

                                userSingleTone.setUserModel(userModel);
                                preferences.create_update_userData(SignInActivity.this,userModel);
                                NavigateToHomeActivity(false,false);
                            }



                        }else
                        {
                            dialog.dismiss();

                            if (response.code() == 402)
                            {
                                Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.inc_ph_pas));
                            }else
                            {
                                CreateSnackBar(getString(R.string.failed));

                            }
                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());

                        }catch (Exception e){}
                    }
                });
    }
    public void signUpWithImage(String name, String email,String phone,String password, Uri avatar_uri)
    {

        final Dialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();

        RequestBody name_part = Common.getRequestBodyText(name);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody email_part = Common.getRequestBodyText(email);
        RequestBody password_part = Common.getRequestBodyText(password);

        try {
            MultipartBody.Part avatar_part = Common.getMultiPart(this,avatar_uri,"avatar");
            Api.getService()
                    .SignUpWithImage(name_part,phone_part,email_part,password_part,avatar_part)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                            if (response.isSuccessful())
                            {
                                dialog.dismiss();
                                DismissSnackBar();

                                if (response.body()!=null)
                                {
                                    UserSingleTone userSingleTone = UserSingleTone.getInstance();
                                    Preferences preferences = Preferences.getInstance();
                                    UserModel userModel = response.body();
                                    userSingleTone.setUserModel(userModel);
                                    preferences.create_update_userData(SignInActivity.this,userModel);

                                    NavigateToHomeActivity(false,true);



                                }else
                                {
                                    Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.failed));
                                }
                            }else
                            {

                                dialog.dismiss();

                                if (response.code()==422)
                                {
                                    Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.phone_number_exists));

                                }else
                                {
                                    CreateSnackBar(getString(R.string.failed));
                                }

                                try {
                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                CreateSnackBar(getString(R.string.something));
                                Log.e("Error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e)
        {
            Toast.makeText(this, R.string.inc_img_path, Toast.LENGTH_SHORT).show();

        }
    }
    public void signUpWithoutImage(String name, String email,String phone,String password)
    {

        final Dialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();

        RequestBody name_part = Common.getRequestBodyText(name);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody email_part = Common.getRequestBodyText(email);
        RequestBody password_part = Common.getRequestBodyText(password);

        Api.getService()
                .SignUpWithoutImage(name_part,phone_part,email_part,password_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            DismissSnackBar();

                            if (response.body()!=null)
                            {
                                UserSingleTone userSingleTone = UserSingleTone.getInstance();
                                Preferences preferences = Preferences.getInstance();
                                UserModel userModel = response.body();
                                userSingleTone.setUserModel(userModel);
                                preferences.create_update_userData(SignInActivity.this,userModel);

                                NavigateToHomeActivity(false,true);



                            }else
                            {
                                Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.failed));
                            }
                        }else
                        {

                            dialog.dismiss();

                            if (response.code()==422)
                            {
                                Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.phone_number_exists));

                            }else
                            {
                                CreateSnackBar(getString(R.string.failed));
                            }

                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void CreateSnackBar(String msg)
    {
        snackbar = Common.CreateSnackBar(this,root,msg);
        snackbar.show();

    }
    private void DismissSnackBar()
    {
        if (snackbar!=null)
        {
            snackbar.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void Back() {

        if (fragment_signIn!=null&&fragment_signIn.isVisible())
        {
            finish();
        }else
            {
                if (fragment_signUp !=null&& fragment_signUp.isVisible())
                {
                    DisplayFragmentSignIn();
                }
            }
    }

    @Override
    public void onBackPressed() {
        Back();
    }



}


