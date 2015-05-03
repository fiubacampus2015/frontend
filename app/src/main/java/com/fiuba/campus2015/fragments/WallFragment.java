package com.fiuba.campus2015.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.campus2015.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;


public class WallFragment extends Fragment
{
    private View myView;


    public static WallFragment newInstance(String param1, String param2) {
        WallFragment fragment = new WallFragment();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.wall_fragment, container, false);

        FloatingActionButton menu = (FloatingActionButton) myView.findViewById(R.id.fab_expand_menu_button);
        menu.setColorNormal(R.color.accent);
        menu.setColorPressed(R.color.black);

        FloatingActionButton button_actionAddPhoto = (FloatingActionButton) myView.findViewById(R.id.action_addphoto);
        button_actionAddPhoto.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddPhoto.setColorNormalResId(R.color.accent);
        button_actionAddPhoto.setColorPressedResId(R.color.black);
        button_actionAddPhoto.setIcon(R.drawable.ic_action_camera);
        button_actionAddPhoto.setStrokeVisible(false);

        FloatingActionButton button_actionAddMeg = (FloatingActionButton) myView.findViewById(R.id.action_write);
        button_actionAddMeg.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddMeg.setColorNormalResId(R.color.accent);
        button_actionAddMeg.setColorPressedResId(R.color.black);
        button_actionAddMeg.setIcon(R.drawable.ic_comment_grey);
        button_actionAddMeg.setStrokeVisible(false);

        FloatingActionButton button_actionAddPlace = (FloatingActionButton) myView.findViewById(R.id.action_addPlace);
        button_actionAddPlace.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddPlace.setColorNormalResId(R.color.accent);
        button_actionAddPlace.setColorPressedResId(R.color.black);
        button_actionAddPlace.setIcon(R.drawable.ic_film_grey);
        button_actionAddPlace.setStrokeVisible(false);

        FloatingActionButton button_actionAddVideo = (FloatingActionButton) myView.findViewById(R.id.action_addVideo);
        button_actionAddVideo.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddVideo.setColorNormalResId(R.color.accent);
        button_actionAddVideo.setColorPressedResId(R.color.black);
        button_actionAddVideo.setIcon(R.drawable.ic_location_on_grey600_18dp);
        button_actionAddVideo.setStrokeVisible(false);

        //final View actionAddPhoto = myView.findViewById(R.id.action_addphoto);

       /* FloatingActionButton actionAddMsg = (FloatingActionButton) myView.findViewById(R.id.action_write);
        actionAddMsg.setTitle("Hide/Show Action above");
        actionAddMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionAddPhoto.setVisibility(actionAddPhoto.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        final FloatingActionButton actionAddVideo = (FloatingActionButton) myView.findViewById(R.id.action_addVideo);
        actionAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionAddVideo.setTitle("Action A clicked");
            }
        });*/

        return myView;
    }
}
