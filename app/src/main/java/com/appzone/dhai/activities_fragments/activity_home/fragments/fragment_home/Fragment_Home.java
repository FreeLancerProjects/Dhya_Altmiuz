package com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.activity.HomeActivity;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class Fragment_Home extends Fragment {
    private AHBottomNavigation ah_bottom;
    private HomeActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }


    public static Fragment_Home newInstance()
    {
        return new Fragment_Home();
    }

    private void initView(View view)
    {
        activity = (HomeActivity) getActivity();
        ah_bottom = view.findViewById(R.id.ah_bottom);
        setUpBottomTabUI();
        activity.DisplayFragmentMain();
        UpdateAHBottomNavigationPosition(0);
        ah_bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                UpdateAHBottomNavigationPosition(position);
                switch (position)
                {
                    case 0:
                        activity.DisplayFragmentMain();

                        break;
                    case 1:
                        activity.DisplayFragmentProfile();

                        break;
                    case 2:
                        activity.DisplayFragmentNotifications();

                        break;
                    case 3:
                        activity.DisplayFragmentOrders();
                        break;
                    case 4:
                        activity.DisplayFragmentMore();
                        break;

                }
                return false;
            }
        });


    }

    private void setUpBottomTabUI()
    {

        ah_bottom.setInactiveColor(ContextCompat.getColor(getActivity(),R.color.white));
        ah_bottom.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        ah_bottom.setTitleTextSizeInSp(15,13);
        ah_bottom.setDefaultBackgroundColor(ContextCompat.getColor(getActivity(),R.color.nav_color));
        ah_bottom.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        ah_bottom.setForceTint(true);
        ah_bottom.setColored(false);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), ContextCompat.getDrawable(getActivity(),R.drawable.nav_home),ContextCompat.getColor(getActivity(),R.color.white));
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.profile), ContextCompat.getDrawable(getActivity(),R.drawable.nav_user),ContextCompat.getColor(getActivity(),R.color.white));
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.notifications), ContextCompat.getDrawable(getActivity(),R.drawable.nav_notification),ContextCompat.getColor(getActivity(),R.color.white));
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.my_orders), ContextCompat.getDrawable(getActivity(),R.drawable.nav_cart),ContextCompat.getColor(getActivity(),R.color.white));
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.more), ContextCompat.getDrawable(getActivity(),R.drawable.nav_menu),ContextCompat.getColor(getActivity(),R.color.white));

        ah_bottom.addItem(item1);
        ah_bottom.addItem(item2);
        ah_bottom.addItem(item3);
        ah_bottom.addItem(item4);
        ah_bottom.addItem(item5);

    }

    public void UpdateAHBottomNavigationPosition(int pos)
    {
        ah_bottom.setCurrentItem(pos,false);
    }

}
