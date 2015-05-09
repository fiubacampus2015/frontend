package com.fiuba.campus2015;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.fragments.CompleteProfile;
import com.fiuba.campus2015.fragments.GroupFragment;
import com.fiuba.campus2015.fragments.WallFragment;

import net.yanzm.mth.MaterialTabHost;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;

import static com.fiuba.campus2015.extras.Constants.USER;


public class ProfileFriend extends ActionBarActivity {
    private Toolbar toolbar;
    public static final int TAB_PROFILE = 1;
    public static final int TAB_WALL = 0;
    public static final int TAB_COUNT = 2;
    private User user;
    private SessionManager session;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friend);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        MaterialTabHost tabHost = (MaterialTabHost) findViewById(R.id.tabs);
        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(pagerAdapter.getPageTitle(i));
        }


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(tabHost);

        tabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String userJson = extras.getString(USER);
            this.user = new Gson().fromJson(userJson, User.class);
            getSupportActionBar().setTitle(user.name + " " + user.username);
        }


        initialize();

    }

    private void initialize() {

    }




    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSubmit = menu.findItem(R.id.action_submit);
        itemSubmit.setVisible(false);
        MenuItem itemSearch = menu.findItem(R.id.action_edit);
        itemSearch.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        return super.onOptionsItemSelected(item);
    }



    class MyPagerAdapter extends FragmentPagerAdapter {

        String[] tabs = getResources().getStringArray(R.array.tabs_friend_profile);
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs_friend_profile);


        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case TAB_WALL:
                    fragment = WallFragment.newInstance("", "");
                    break;
                case TAB_PROFILE:
                    fragment = CompleteProfile.newInstance(user);
                    break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }


        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}