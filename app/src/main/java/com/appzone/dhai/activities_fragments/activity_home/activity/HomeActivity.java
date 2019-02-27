package com.appzone.dhai.activities_fragments.activity_home.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_About_Us;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Contact;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Home;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Jobs;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Offers;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Other_Services;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Services;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Student;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Training;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_Main;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_More;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_Notifications;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_Profile;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.fragment_orders.Fragment_Orders;
import com.appzone.dhai.activities_fragments.activity_sign_in.activity.SignInActivity;
import com.appzone.dhai.activities_fragments.activity_terms_conditions.TermsConditionsActivity;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.preferences.Preferences;
import com.appzone.dhai.remote.Api;
import com.appzone.dhai.share.Common;
import com.appzone.dhai.singletone.UserSingleTone;
import com.appzone.dhai.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout ll_back;
    private ImageView image_arrow;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_Jobs fragment_jobs;
    private Fragment_Offers fragment_offers;
    private Fragment_Student fragment_student;
    private Fragment_Training fragment_training;
    private Fragment_Services fragment_services;
    private Fragment_Other_Services fragment_other_services;
    private Fragment_Contact fragment_contact;
    private Fragment_About_Us fragment_about_us;

    ///////////////////////////////////////
    private Fragment_Main fragment_main;
    private Fragment_Profile fragment_profile;
    private Fragment_More fragment_more;
    private Fragment_Notifications fragment_notifications;
    private Fragment_Orders fragment_orders;
    ////////////////////////////////////////////////
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        fragmentManager = getSupportFragmentManager();
        DisplayFragmentHome();
    }

    private void initView() {
        userSingleTone = UserSingleTone.getInstance();
        preferences = Preferences.getInstance();
        String session = preferences.getSession(this);
        if (session.equals(Tags.session_login))
        {
            userSingleTone.setUserModel(preferences.getUserData(this));
        }
        userModel = userSingleTone.getUserModel();

        ll_back = findViewById(R.id.ll_back);
        image_arrow = findViewById(R.id.image_arrow);

        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            image_arrow.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_arrow.setImageResource(R.drawable.arrow_left);

        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
    }

    public void updateUserData(UserModel userModel)
    {
        this.userModel = userModel;
    }

    public void DisplayFragmentHome() {

        if (fragment_home == null)
        {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_home).commit();
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container,fragment_home,"fragment_home").addToBackStack("fragment_home").commit();
            }

    }

    public void DisplayFragmentMain()
    {


        if (fragment_profile!=null && fragment_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (fragment_contact!=null && fragment_contact.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_contact).commit();
        }

        if (fragment_more!=null && fragment_more.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }


        if (fragment_main==null)
        {
            fragment_main = Fragment_Main.newInstance();
        }

        if (fragment_main.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_main).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_main,"fragment_main").addToBackStack("fragment_main").commit();
        }

        if (fragment_home!=null && fragment_home.isAdded())
        {
            fragment_home.UpdateAHBottomNavigationPosition(0);

        }

    }

    public void DisplayFragmentProfile()
    {


        if (fragment_main!=null && fragment_main.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_contact!=null && fragment_contact.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_contact).commit();
        }

        if (fragment_more!=null && fragment_more.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }


        if (fragment_profile==null)
        {
            fragment_profile = Fragment_Profile.newInstance();
        }

        if (fragment_profile.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_profile).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_profile,"fragment_profile").addToBackStack("fragment_profile").commit();
        }
        if (fragment_home!=null && fragment_home.isAdded())
        {
            fragment_home.UpdateAHBottomNavigationPosition(1);

        }

    }

    public void DisplayFragmentContact()
    {

        if (fragment_contact == null)
        {
            fragment_contact = Fragment_Contact.newInstance();
        }

        if (fragment_contact.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_contact).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container,fragment_contact,"fragment_contact").addToBackStack("fragment_contact").commit();
        }


    }

    public void DisplayFragmentNotifications()
    {
        if (fragment_main!=null && fragment_main.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_profile!=null && fragment_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }

        if (fragment_more!=null && fragment_more.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }


        if (fragment_notifications==null)
        {
            fragment_notifications = Fragment_Notifications.newInstance();
        }

        if (fragment_notifications.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_notifications).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_notifications,"fragment_notifications").addToBackStack("fragment_notifications").commit();
        }
        if (fragment_home!=null && fragment_home.isAdded())
        {
            fragment_home.UpdateAHBottomNavigationPosition(2);

        }
    }

    public void DisplayFragmentOrders()
    {
        if (fragment_main!=null && fragment_main.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_profile!=null && fragment_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }

        if (fragment_more!=null && fragment_more.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }


        if (fragment_orders==null)
        {
            fragment_orders = Fragment_Orders.newInstance();
        }

        if (fragment_orders.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_orders).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_orders,"fragment_orders").addToBackStack("fragment_orders").commit();
        }
        if (fragment_home!=null && fragment_home.isAdded())
        {
            fragment_home.UpdateAHBottomNavigationPosition(3);

        }
    }


    public void DisplayFragmentMore()
    {


        if (fragment_main!=null && fragment_main.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_profile!=null && fragment_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }

        if (fragment_contact!=null && fragment_contact.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_contact).commit();
        }


        if (fragment_more==null)
        {
            fragment_more = Fragment_More.newInstance();
        }

        if (fragment_more.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_more,"fragment_more").addToBackStack("fragment_more").commit();
        }
        if (fragment_home!=null && fragment_home.isAdded())
        {
            fragment_home.UpdateAHBottomNavigationPosition(3);

        }

    }

    public void DisplayFragmentJobs()
    {
        if (fragment_jobs == null)
        {
            fragment_jobs = Fragment_Jobs.newInstance();
        }

        if (fragment_jobs.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_jobs).commit();
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container,fragment_jobs,"fragment_jobs").addToBackStack("fragment_jobs").commit();
            }
    }
    public void DisplayFragmentOffers()
    {
        if (fragment_offers == null)
        {
            fragment_offers = Fragment_Offers.newInstance();
        }

        if (fragment_offers.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_offers).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container,fragment_offers,"fragment_offers").addToBackStack("fragment_offers").commit();
        }
    }

    public void DisplayFragmentOtherService()
    {
        if (fragment_other_services == null)
        {
            fragment_other_services = Fragment_Other_Services.newInstance();
        }

        if (fragment_other_services.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_other_services).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container,fragment_other_services,"fragment_other_services").addToBackStack("fragment_other_services").commit();
        }
    }
    public void DisplayFragmentService()
    {
        if (fragment_services == null)
        {
            fragment_services = Fragment_Services.newInstance();
        }

        if (fragment_services.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_services).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container,fragment_services,"fragment_services").addToBackStack("fragment_services").commit();
        }
    }

    public void DisplayFragmentStudents()
    {
        if (fragment_student == null)
        {
            fragment_student = Fragment_Student.newInstance();
        }

        if (fragment_student.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_student).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container,fragment_student,"fragment_student").addToBackStack("fragment_student").commit();
        }
    }

    public void DisplayFragmentTraining()
    {
        if (fragment_training == null)
        {
            fragment_training = Fragment_Training.newInstance();
        }

        if (fragment_training.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_training).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container,fragment_training,"fragment_training").addToBackStack("fragment_training").commit();
        }
    }

    public void DisplayFragmentAboutUs()
    {
        if (fragment_about_us == null)
        {
            fragment_about_us = Fragment_About_Us.newInstance();
        }

        if (fragment_about_us.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_about_us).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container,fragment_about_us,"fragment_about_us").addToBackStack("fragment_about_us").commit();
        }
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back()
    {

        if (fragment_jobs != null &&fragment_jobs.isVisible())
        {
            fragmentManager.beginTransaction().hide(fragment_jobs).commit();
        }else if (fragment_offers !=null && fragment_offers.isVisible())
        {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();

        }else if (fragment_student !=null && fragment_student.isVisible())
        {
            fragmentManager.beginTransaction().hide(fragment_student).commit();

        }
        else if (fragment_training !=null && fragment_training.isVisible())
        {
            fragmentManager.beginTransaction().hide(fragment_training).commit();

        }
        else if (fragment_services !=null && fragment_services.isVisible())
        {
            fragmentManager.beginTransaction().hide(fragment_services).commit();

        }
        else if (fragment_other_services !=null && fragment_other_services.isVisible())
        {
            fragmentManager.beginTransaction().hide(fragment_other_services).commit();

        }else if (fragment_contact !=null && fragment_contact.isVisible())
        {
            super.onBackPressed();

        }
        else if (fragment_about_us !=null && fragment_about_us.isVisible())
        {
            super.onBackPressed();

        }

        else if (fragment_main!=null && fragment_main.isVisible())
        {
           finish();
        }
        else
        {
            DisplayFragmentMain();
        }




    }

    public void Logout()
    {
        final Dialog dialog = Common.createProgressDialog(this,getString(R.string.logging_out));
        dialog.show();
        Api.getService()
                .logout(userModel.getToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            if (manager!=null)
                                            {
                                                manager.cancelAll();
                                            }
                                        }
                                    },1);
                            userModel = null;
                            userSingleTone.clear();
                            dialog.dismiss();
                            NavigateToSignInActivity();

                        }else
                        {
                            dialog.dismiss();

                            Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void NavigateToSignInActivity()
    {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();

    }

    public void NavigateToTermsCondition()
    {
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        startActivity(intent);
    }
}
