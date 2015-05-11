package com.fiuba.campus2015.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.session.SessionManager;

import net.yanzm.mth.MaterialTabHost;


public class MyBoard extends Fragment {

    public static final int TAB_CONTACTS = 1;
    public static final int TAB_WALL = 0;
    public static final int TAB_GROUPS = 2;
    public static final int TAB_COUNT = 3;
    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.myboard, container, false);

        session = new SessionManager(view.getContext());

        MaterialTabHost tabHost = (MaterialTabHost) view.findViewById(R.id.tabs);
        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
//        tabHost.setType(MaterialTabHost.Type.Centered);
//        tabHost.setType(MaterialTabHost.Type.LeftOffset);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getFragmentManager());
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(pagerAdapter.getPageTitle(i));
        }

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(tabHost);

        tabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });
        return view;
    }
    class MyPagerAdapter extends FragmentPagerAdapter{

        String[] tabs = getResources().getStringArray(R.array.tabs);
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);


        }

        @Override
        public Fragment getItem(int position) {
           // MyFragment myFragment = MyFragment.getInstance(position);
           // return myFragment;

            Fragment fragment = null;
            switch (position) {
                case TAB_WALL:
                    fragment = WallFragment.newInstance(session.getUserid());
                    break;
                case TAB_CONTACTS:
                    fragment = ContactFragment.newInstance("", "");
                    break;
                case TAB_GROUPS:
                    fragment = GroupFragment.newInstance("", "");
                    break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return getResources().getStringArray(R.array.tabs)[position];
            return tabs[position];
        }


        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

}
