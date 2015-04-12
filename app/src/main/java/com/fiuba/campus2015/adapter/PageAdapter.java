package com.fiuba.campus2015.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.fragments.CommentsFragment;
import com.fiuba.campus2015.fragments.EducationFragment;
import com.fiuba.campus2015.fragments.EmpleoFragment;
import com.fiuba.campus2015.fragments.LoadPhoto;
import com.fiuba.campus2015.fragments.PersonalDataFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ismael on 09/04/15.
 */
public class PageAdapter extends FragmentPagerAdapter {
    private int NUM_ITEMS = 4;
    List<String> title;
    private User user;
    // solucion temporal
    private PersonalDataFragment personalDataFragment;
    private EducationFragment educationFragment;
    private EmpleoFragment empleoFragment;
    private CommentsFragment commentsFragment;

    public PageAdapter(FragmentManager fm, User user) {
        super(fm);
        title = new ArrayList<>();
        title.add("DATOS PERSONALES");
        title.add("EDUCACIÓN");
        title.add("EMPLEO");
        title.add("COMENTARIOS");
        this.user = user;

    }

    // se obtienen los datos de todas las pantallas
    public Bundle getAllData() {
        Bundle bundle = new Bundle();
        bundle.putAll(personalDataFragment.getData());
        bundle.putAll(educationFragment.getData());
        bundle.putAll(empleoFragment.getData());
        bundle.putAll(commentsFragment.getData());

        return  bundle;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                personalDataFragment = PersonalDataFragment.newInstance(this.user);
                return personalDataFragment;
            case 1:
                educationFragment = EducationFragment.newInstance(this.user.education);
                return educationFragment;
            case 2:
                empleoFragment = new EmpleoFragment();
                return empleoFragment;
            case 3:
                commentsFragment = CommentsFragment.newInstance(this.user.personal.comments);
                return commentsFragment;
            default: return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
