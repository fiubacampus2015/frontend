package com.fiuba.campus2015.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.PageAdapter;

/**
 * Created by ismael on 09/04/15.
 */
public class LoadFragment extends Fragment{
    private PageAdapter adapterViewPager;
    private ViewPager vpPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.load, container, false);

        // obs: childfragment
        adapterViewPager = new PageAdapter(getChildFragmentManager());
        vpPager = (ViewPager) view.findViewById(R.id.vpPager);
        vpPager.setAdapter(adapterViewPager);




        return view;
    }

    // necesario para devolver la foto elegida al fragment hijo
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
