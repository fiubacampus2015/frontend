package com.fiuba.campus2015.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fiuba.campus2015.fragments.EducationFragment;
import com.fiuba.campus2015.fragments.LoadPhoto;
import com.fiuba.campus2015.fragments.PersonalDataFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ismael on 09/04/15.
 */
public class PageAdapter extends FragmentPagerAdapter {
    private int NUM_ITEMS = 3; // por ahora hay 3
    List<String> title;

    public PageAdapter(FragmentManager fm) {
        super(fm);
        title = new ArrayList<>();
        title.add("DATOS PERSONALES");
        title.add("FOTO");
        title.add("EDUCACIÓN");
        title.add("EMPLEO");
        title.add("COMENTARIOS");
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0: return new PersonalDataFragment();
            case 1: return new LoadPhoto();
            case 2: return new EducationFragment();
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
