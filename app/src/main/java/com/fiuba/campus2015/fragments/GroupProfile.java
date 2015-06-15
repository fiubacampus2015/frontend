package com.fiuba.campus2015.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.controller.IMaterialListAdapter;
import com.dexafree.materialList.controller.MaterialListAdapter;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.customcard.RequestCard;
import com.fiuba.campus2015.customcard.TextCard;
import com.fiuba.campus2015.dto.user.File;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.MemberShip;
import com.fiuba.campus2015.dto.user.Subscription;
import com.fiuba.campus2015.dto.user.Subscriptions;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.fiuba.campus2015.extras.Constants.DESCRIPCIONGRUPO;
import static com.fiuba.campus2015.extras.Constants.GROUP;
import static com.fiuba.campus2015.extras.Constants.GROUP_TOTALCONTACTS;
import static com.fiuba.campus2015.extras.Constants.GROUP_TOTALFILES;
import static com.fiuba.campus2015.extras.Constants.GROUP_TOTALMSGS;
import static com.fiuba.campus2015.extras.Constants.NAME;
import static com.fiuba.campus2015.extras.Constants.PHOTO;
import static com.fiuba.campus2015.extras.Constants.GROUPOWNER;
import static com.fiuba.campus2015.extras.Constants.GROUPDATE;

public class GroupProfile extends Fragment {
    private  View view;
    private MaterialListView profileInformation;
    private Group group;
    private int participantes;
    private int messages;
    private int files;
    private TextCard ownerCard;
    private String groupId;
    private SessionManager session;
    private ProgressBar prgrsBar;

    public static GroupProfile newInstance(Group group) {
        GroupProfile fragment = new GroupProfile();

        Bundle args = new Bundle();
        args.putString(NAME, group.name);
        args.putString(GROUP, group._id);
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
        view =  inflater.inflate(R.layout.group_profile, container, false);
        prgrsBar = (ProgressBar) view.findViewById(R.id.progressBarCircularIndeterminateGroupProfile);

        profileInformation = (MaterialListView) view.findViewById(R.id.groupProfileInfo);

        session = new SessionManager(getActivity().getApplicationContext());
        groupId = getArguments().getString(GROUP);
        load();
        getSubscriptors();
        return view;
    }

    public void acceptRequest(String _id) {
        resolveSubscription("accepted",_id);
        updateParticipantes();
    }
    public void rejectRequest(String _id) {
        resolveSubscription("denegada", _id);
    }


    private void updateParticipantes() {
        participantes++;
        ownerCard.setDescription(getParticipantes() + getMessages() + getFiles());

        profileInformation.getAdapter().notifyDataSetChanged();
    }

    private String getParticipantes() {
        return "#" + Integer.toString(participantes) + " participantes.\n";
    }

    private String getMessages() {
        return "#" + Integer.toString(messages) + " mensajes.\n";
    }

    private String getFiles() {
        return "#" + Integer.toString(files) + " archivos.";
    }


    private void load() {

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

        participantes = Integer.parseInt(getArguments().getString(GROUP_TOTALCONTACTS));
        messages = Integer.parseInt(getArguments().getString(GROUP_TOTALMSGS));
        files = Integer.parseInt(getArguments().getString(GROUP_TOTALFILES));

        ownerCard = new TextCard(view.getContext());
        ownerCard.setDescription("#" + getArguments().getString(GROUP_TOTALCONTACTS) + " participantes. " + "\n#" + getArguments().getString(GROUP_TOTALMSGS) + " mensajes." + "\n#" + getArguments().getString(GROUP_TOTALFILES) + " archivos.");
        ownerCard.setDismissible(false);
        ownerCard.setTag("SMALL_IMAGE_CARD");
        ownerCard.hideComponents();

        profileInformation.add(ownerCard);

        //addRequest();
    }


    private void addRequest(Subscription subs) {

        RequestCard requestCard = new RequestCard(view.getContext());
        requestCard.setTitle(subs.user.name+' '+subs.user.username);
        requestCard.setDescription(subs.user.email);
        requestCard.setPhoto(subs.user.personal.photo);

        requestCard.setTag("REQUEST_TAG");

        addListener(requestCard, subs._id);



        profileInformation.add(requestCard);
    }

    private void addListener(final RequestCard card2, final String _id) {
        card2.setOnButtonPressedAccpetListener(new OnButtonPressListener() {
            @Override
            public void onButtonPressedListener(View view, Card card) {
                card.setDismissible(true);
                profileInformation.remove(card);
                acceptRequest(_id);
            }
        });

        card2.setOnButtonPressedRejectListener(new OnButtonPressListener() {
            @Override
            public void onButtonPressedListener(View view, Card card) {
                card.setDismissible(true);
                profileInformation.remove(card);
                rejectRequest(_id);
            }
        });
    }


    //Se llama a este metodo en caso de que no haya error
    @Subscribe
    public void onSubscriptions(Subscriptions subs) {

        if (!subs.subscriptions.isEmpty())
        {
            for (int i = 0; i < subs.subscriptions.size(); i++) {
                addRequest(subs.subscriptions.get(i));
            }

        }
        Application.getEventBus().unregister(this);
    }


    @Subscribe
    public void onResolve(MemberShip member) {
        Application.getEventBus().unregister(this);
    }

    public void getSubscriptors() {
        Application.getEventBus().register(this);

        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<Subscriptions, IApiUser>() {
            @Override
            public Subscriptions getResult(IApiUser service) {
                return service.getSuscriptors(session.getToken(), groupId);
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<Subscriptions, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }

    public void resolveSubscription(final String status, final String subsId) {
        Application.getEventBus().register(this);

        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<MemberShip, IApiUser>() {
            @Override
            public MemberShip getResult(IApiUser service) {
                return service.subscribeResolve(session.getToken(), groupId, subsId, new MemberShip(status));
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<MemberShip, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }


    @Override
    public void onResume() {
        super.onResume();

        if(group != null) {
            BigImageCard card = (BigImageCard)((IMaterialListAdapter) profileInformation.getAdapter()).getCard(0);

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
