package com.fiuba.campus2015.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.campus2015.R;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.adapter.MessageAdapter;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Constants;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class WallFragment extends Fragment
{
    private View myView;
    private MaterialListView mListView;
    private MessageAdapter msgAdapter;


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

        FloatingActionButton button_actionAddPhoto = (FloatingActionButton) myView.findViewById(R.id.action_addphoto);
        button_actionAddPhoto.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddPhoto.setColorNormalResId(R.color.accent);
        button_actionAddPhoto.setColorPressedResId(R.color.black);
        button_actionAddPhoto.setIcon(R.drawable.ic_camera_grey600_48dp);
        button_actionAddPhoto.setStrokeVisible(false);

        FloatingActionButton button_actionAddMeg = (FloatingActionButton) myView.findViewById(R.id.action_write);
        button_actionAddMeg.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddMeg.setColorNormalResId(R.color.accent);
        button_actionAddMeg.setColorPressedResId(R.color.black);
        button_actionAddMeg.setIcon(R.drawable.ic_rate_review_grey600_36dp);
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


        mListView = (MaterialListView) myView.findViewById(R.id.material_listview);

        this.msgAdapter = new MessageAdapter(myView.getContext(), mListView);
        msgAdapter.setData(getMessageItemsMock());
        msgAdapter.fillArray();

        return myView;
    }


    private List<Message> getMessageItemsMock(){

        List<Message> mensajes = new ArrayList<Message>();
        User user = new User("Jimena", "Tapia", "user", "pwd", null);
        mensajes.add(new Message("Esto es un mensaje del muro. Estoy probando el contenido y formato de las distintas cards. Parece lindo!",user,"Lunes 4 de Mayo", Constants.MsgCardType.text));
        mensajes.add(new Message("Esta soy yo!",user,"Lunes 4 de Mayo", Constants.MsgCardType.photo));
        mensajes.add(new Message("miren esto que copado!",user,"Lunes 4 de Mayo", Constants.MsgCardType.video));
        mensajes.add(new Message("Casa - Villa Urquiza",user,"Lunes 4 de Mayo", Constants.MsgCardType.place));
        mensajes.add(new Message("Otro mensajes pero mas cortito.",user,"Lunes 4 de Mayo", Constants.MsgCardType.text));

        return mensajes;
    }


    }

