package com.fiuba.campus2015.fragments;

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
public class EmpleoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.jobfragment, container, false);

        return view;
    }
}
