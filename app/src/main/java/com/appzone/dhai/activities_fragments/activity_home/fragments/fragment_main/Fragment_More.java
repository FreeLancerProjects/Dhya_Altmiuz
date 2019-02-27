package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;

import java.util.Locale;

public class Fragment_More extends Fragment {

    private LinearLayout ll_terms,ll_bank_account,ll_about,ll_developer,ll_contact;
    private ImageView arrow1,arrow2,arrow3,arrow4,arrow5;
    private String current_language;
    private HomeActivity activity;

    public static Fragment_More newInstance()
    {
        return new Fragment_More();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);

        if (current_language.equals("ar"))
        {
            arrow1.setImageResource(R.drawable.black_left_arrow);
            arrow2.setImageResource(R.drawable.black_left_arrow);
            arrow3.setImageResource(R.drawable.black_left_arrow);
            arrow4.setImageResource(R.drawable.black_left_arrow);
            arrow5.setImageResource(R.drawable.black_left_arrow);



        }else
        {
            arrow1.setImageResource(R.drawable.black_right_arrow);
            arrow2.setImageResource(R.drawable.black_right_arrow);
            arrow3.setImageResource(R.drawable.black_right_arrow);
            arrow4.setImageResource(R.drawable.black_right_arrow);
            arrow5.setImageResource(R.drawable.black_right_arrow);

        }

        ll_terms = view.findViewById(R.id.ll_terms);
        ll_bank_account = view.findViewById(R.id.ll_bank_account);
        ll_about = view.findViewById(R.id.ll_about);
        ll_developer = view.findViewById(R.id.ll_developer);
        ll_contact = view.findViewById(R.id.ll_contact);

        ll_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToTermsCondition();
            }
        });

        ll_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentContact();
            }
        });

        ll_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentAboutUs();
            }
        });

    }
}
