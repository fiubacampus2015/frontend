package com.fiuba.campus2015.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.fragments.CommentsFragment;
import com.fiuba.campus2015.fragments.EducationFragment;
import com.fiuba.campus2015.fragments.EmpleoFragment;
import com.fiuba.campus2015.fragments.PersonalProfile;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    private int NUM_ITEMS = 4;
    private User user;
    private PersonalProfile personalProfile;
    private EducationFragment educationFragment;
    private EmpleoFragment empleoFragment;
    private CommentsFragment commentsFragment;

    public ProfileAdapter(FragmentManager fm, User user) {
        super(fm);
        titles = new ArrayList<>();
        titles.add("DATOS PERSONALES");
        titles.add("EDUCACIÓN");
        titles.add("EMPLEO");
        titles.add("COMENTARIOS");

        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                personalProfile = PersonalProfile.newInstance(user);
                return  personalProfile;
            case 1:
                educationFragment = EducationFragment.newInstance(this.user.education);
                educationFragment.disable();
                return educationFragment;
            case 2:
                empleoFragment =  EmpleoFragment.newInstance(this.user.job);
                empleoFragment.disable();
                return empleoFragment;
            case 3:
                commentsFragment = CommentsFragment.newInstance(this.user.personal.comments);
                commentsFragment.disable();
                return commentsFragment;
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
