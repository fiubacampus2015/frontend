package com.fiuba.campus2015.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.model.Card;
import com.fiuba.campus2015.Map;
import com.fiuba.campus2015.ProfileFriend;
import com.fiuba.campus2015.R;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.adapter.MessageAdapter;
import com.fiuba.campus2015.customcard.VideoCard;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.ProgressDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;

import static com.fiuba.campus2015.extras.Constants.USERTO;

public class WallFragment extends Fragment
{
    private View myView;
    private MaterialListView mListView;
    private MessageAdapter msgAdapter;
    private WriteMsgDialog w_msgDialog;
    private VideoDialog videoDialog;
    private SessionManager session;
    private ProgressBar prgrsBar;

    public static WallFragment newInstance(String userId) {
        WallFragment fragment = new WallFragment();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        args.putString(USERTO, userId);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.wall_fragment, container, false);

        session = new SessionManager(getActivity().getApplicationContext());

        FloatingActionButton button_actionAddPhoto = (FloatingActionButton) myView.findViewById(R.id.action_addphoto);
        final FloatingActionButton button_actionAddMeg = (FloatingActionButton) myView.findViewById(R.id.action_write);


        w_msgDialog = new WriteMsgDialog(getActivity(), this, getArguments().getString(USERTO));
        videoDialog = new VideoDialog(getActivity(), this, getArguments().getString(USERTO));

        FloatingActionButton button_actionAddPlace = (FloatingActionButton) myView.findViewById(R.id.action_addPlace);
        FloatingActionButton button_actionAddVideo = (FloatingActionButton) myView.findViewById(R.id.action_addVideo);


        button_actionAddMeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w_msgDialog.showDialog();
            }
        });

        mListView = (MaterialListView) myView.findViewById(R.id.material_listview);

        button_actionAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDialog.showDialog();
            }
        });

        button_actionAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Map.class);
                startActivity(intent);
            }
        });

        this.msgAdapter = new MessageAdapter(myView.getContext(), mListView, getArguments().getString(USERTO),this);
        prgrsBar = (ProgressBar) myView.findViewById(R.id.progressBarCircularIndeterminate_);
        update();

        return myView;
    }

    public void update() {
        GetWallMsgsTask fillWall = new GetWallMsgsTask();
        fillWall.execute();
    }

    private List<Message> getMessageItemsMock(){

        List<Message> mensajes = new ArrayList<Message>();
        User user = new User("Jimena", "Tapia", "user", "pwd", null);
       // mensajes.add(new Message("Esto es un mensaje del muro. Estoy probando el contenido y formato de las distintas cards. Parece lindo!","Lunes 4 de Mayo", Constants.MsgCardType.text));
       // mensajes.add(new Message("Esta soy yo!","Lunes 4 de Mayo", Constants.MsgCardType.photo));
       // mensajes.add(new Message("miren esto que copado!","Lunes 4 de Mayo", Constants.MsgCardType.video));
       // mensajes.add(new Message("Casa - Villa Urquiza","Lunes 4 de Mayo", Constants.MsgCardType.place));
        //mensajes.add(new Message("Otro mensajes pero mas cortito.","Lunes 4 de Mayo", Constants.MsgCardType.text));

        return mensajes;
    }

    public void onRelativePathVideo(int codeRequest, Intent intent) {
        startActivityForResult(intent, codeRequest);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            videoDialog.setUri(data.getData());
        }
    }

    // probando video
    public void addVideoCard(Message msg, Drawable videoPreview, Uri uriVideo) {
        msgAdapter.setVideoTest(msg, videoPreview, uriVideo);
    }

    private class GetWallMsgsTask extends AsyncTask<Void, Void,
            List<Message>> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
            prgrsBar.setEnabled(true);
            prgrsBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Message> doInBackground(Void... params) {

            List<Message> msgs = null;
            IApiUser api = restAdapter.create(IApiUser.class);
            try {
                msgs = api.getUserWallMessages(session.getToken(), getArguments().getString(USERTO));

            } catch (Exception x) {}

            return msgs;
        }

        @Override
        protected void onPostExecute(List<Message> msgs) {
            prgrsBar.setVisibility(View.INVISIBLE);
            if(msgs!=null) {
                msgAdapter.setData(msgs);
                msgAdapter.fillArray();
            }
        }
    }

}

