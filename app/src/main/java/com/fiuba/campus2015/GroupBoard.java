package com.fiuba.campus2015;


import android.content.Intent;
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

import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.fragments.GroupContactFragment;
import com.fiuba.campus2015.fragments.GroupFilesFragment;
import com.fiuba.campus2015.fragments.GroupForumsFragment;
import com.fiuba.campus2015.fragments.GroupProfile;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;

import net.yanzm.mth.MaterialTabHost;

import static com.fiuba.campus2015.extras.Constants.GROUP;


public class GroupBoard extends ActionBarActivity {
    private Toolbar toolbar;
    public static final int TAB_INFO = 0;
    public static final int TAB_FORUMS = 1;
    public static final int TAB_FILES = 2;
    public static final int TAB_CONTACTS = 3;
    public static final int TAB_COUNT = 4;
    private Group group;
    private SessionManager session;
    private final int CODEUPDATE = 28;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_board);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String groupJson = extras.getString(GROUP);
            this.group = new Gson().fromJson(groupJson, Group.class);
            getSupportActionBar().setTitle(group.name);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        MaterialTabHost tabHost = (MaterialTabHost) findViewById(R.id.tabs);
        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setGroup(this.group);

        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(pagerAdapter.getPageTitle(i));
        }

        tabHost.setCurrentTab(TAB_INFO);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(TAB_INFO);
        viewPager.setOnPageChangeListener(tabHost);

        tabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });

    }

    private boolean isOwner() {
        User user = group.owner;
        return (session.getUserid().equals(user._id)) ? true : false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, ModifyGroup.class);
                intent.putExtra(GROUP, new Gson().toJson(group));
                startActivityForResult(intent, CODEUPDATE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSubmit = menu.findItem(R.id.action_submit);
        itemSubmit.setVisible(false);
        MenuItem itemSearch = menu.findItem(R.id.action_edit);

        if(isOwner())
            itemSearch.setVisible(true);
        else
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODEUPDATE && resultCode == RESULT_OK && null != data) {
            Group groupUpdate = new Gson().fromJson(data.getExtras().getString(GROUP), Group.class);

            // esto para que seactualice la barra de titulo al regresar de modificar
            getSupportActionBar().setTitle(groupUpdate.name);
            String photo = groupUpdate.photo;
            if(photo != null && !photo.isEmpty()) {
                group.photo = photo;
            }
            group.name = groupUpdate.name;
            group.description = groupUpdate.description;
        }
    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        private Group group;
        String[] tabs = getResources().getStringArray(R.array.tabs_group_board);
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs_group_board);


        }

        public void setGroup(Group group)
        {
            this.group = group;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
              case TAB_CONTACTS:
                    fragment = GroupContactFragment.newInstance(group);
                    break;
                case TAB_FILES:
                    fragment = GroupFilesFragment.newInstance(group);
                    break;
                case TAB_FORUMS:
                    fragment = GroupForumsFragment.newInstance(group);
                    break;
                case TAB_INFO:
                    fragment = GroupProfile.newInstance(group);
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