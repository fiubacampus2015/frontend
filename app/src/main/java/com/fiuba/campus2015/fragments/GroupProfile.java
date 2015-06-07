package com.fiuba.campus2015.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.controller.IMaterialListAdapter;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.customcard.TextCard;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.services.Application;
import com.squareup.otto.Subscribe;

import static com.fiuba.campus2015.extras.Constants.DESCRIPCIONGRUPO;
import static com.fiuba.campus2015.extras.Constants.GROUP_TOTALCONTACTS;
import static com.fiuba.campus2015.extras.Constants.GROUP_TOTALFILES;
import static com.fiuba.campus2015.extras.Constants.GROUP_TOTALMSGS;
import static com.fiuba.campus2015.extras.Constants.NAME;
import static com.fiuba.campus2015.extras.Constants.PHOTO;
import static com.fiuba.campus2015.extras.Constants.GROUPOWNER;
import static com.fiuba.campus2015.extras.Constants.GROUPDATE;

public class GroupProfile extends Fragment {

    private MaterialListView profileInformation;
    private Group group;

    public static GroupProfile newInstance(Group group) {
        GroupProfile fragment = new GroupProfile();

        Bundle args = new Bundle();
        args.putString(NAME, group.name);
        args.putString(DESCRIPCIONGRUPO, group.description);
        args.putString(PHOTO, group.photo);
        args.putString(GROUPOWNER, group.owner.name + " " + group.owner.username);
        args.putString(GROUPDATE, group.date);
        args.putString(GROUP_TOTALCONTACTS, (group.members != null ? group.members.toString() : "0"));
        args.putString(GROUP_TOTALMSGS, (group.msgs != null ? group.msgs.toString() : "0"));
        args.putString(GROUP_TOTALFILES, (group.totalFiles != null ? group.totalFiles.toString() : "0"));

        fragment.setArguments(args);

        return fragment;
    }

    @Subscribe
    public void update(Group group) {
        this.group = group;
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
        Application.getEventBus().register(this);
        return view;
    }

    private void load(View view) {

        BigImageCard personalCard = new BigImageCard(view.getContext());
        personalCard.setTitle(getArguments().getString(NAME));
        personalCard.setTitleColorRes(R.color.black_button);
        personalCard.setDescription(getArguments().getString(DESCRIPCIONGRUPO) + "\n" + getArguments().getString(GROUPOWNER));
        personalCard.setTag("BIG_IMAGE_CARD");

        if (getArguments().getString(PHOTO) != null && !getArguments().getString(PHOTO).isEmpty()) {
            Drawable drawable = new BitmapDrawable(getResources(), Utils.getPhoto(getArguments().getString(PHOTO)));
            personalCard.setDrawable(drawable);

        }else
            personalCard.setDrawable(R.drawable.profiledefault);

        personalCard.setDismissible(false);

        profileInformation.add(personalCard);

        TextCard ownerCard = new TextCard(view.getContext());
        ownerCard.setDescription("#" + getArguments().getString(GROUP_TOTALCONTACTS) + " participantes. " + "\n#" + getArguments().getString(GROUP_TOTALMSGS) + " mensajes." + "\n#" + getArguments().getString(GROUP_TOTALFILES) + " archivos.");
        ownerCard.setDismissible(false);
        ownerCard.setTag("SMALL_IMAGE_CARD");

        profileInformation.add(ownerCard);

    }

    @Override
    public void onResume() {
        super.onResume();

        BigImageCard card = (BigImageCard)((IMaterialListAdapter) profileInformation.getAdapter()).getCard(0);

        if(group != null) {
            String photo = group.photo;

            if(photo != null && !photo.isEmpty()) {
                Drawable drawable = new BitmapDrawable(getResources(), Utils.getPhoto(photo));
                card.setDrawable(drawable);
            }
            card.setTitle(group.name);
            card.setDescription(group.description);

            group = null;
        }
    }
}
