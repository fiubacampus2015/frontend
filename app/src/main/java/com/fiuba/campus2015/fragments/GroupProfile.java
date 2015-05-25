package com.fiuba.campus2015.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.extras.Utils;

import static com.fiuba.campus2015.extras.Constants.DESCRIPCIONGRUPO;
import static com.fiuba.campus2015.extras.Constants.NAME;
import static com.fiuba.campus2015.extras.Constants.PHOTO;

public class GroupProfile extends Fragment {

    private MaterialListView profileInformation;


    public static GroupProfile newInstance(Group group) {
        GroupProfile fragment = new GroupProfile();

        Bundle args = new Bundle();
        args.putString(NAME, group.name);
        args.putString(DESCRIPCIONGRUPO, group.description);
        args.putString(PHOTO, group.photo);

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.group_profile, container, false);

        profileInformation = (MaterialListView) view.findViewById(R.id.groupProfileInfo);

        load(view);

        return view;
    }

    private void load(View view) {

        BigImageCard personalCard = new BigImageCard(view.getContext());
        personalCard.setDescription(getArguments().getString(NAME) + "\n" + getArguments().getString(DESCRIPCIONGRUPO));
        personalCard.setTag("BIG_IMAGE_CARD");

        if (getArguments().getString(PHOTO) != null && !getArguments().getString(PHOTO).isEmpty()) {
            Drawable drawable = new BitmapDrawable(getResources(), Utils.getPhoto(getArguments().getString(PHOTO)));
            personalCard.setDrawable(drawable);

        }else
            personalCard.setDrawable(R.drawable.profiledefault);

        personalCard.setDismissible(false);

        profileInformation.add(personalCard);

    }

}
