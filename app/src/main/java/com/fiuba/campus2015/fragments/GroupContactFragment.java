package com.fiuba.campus2015.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.ProfileFriend;
import com.fiuba.campus2015.ProfileReduced;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.ContactsAdapter;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

import static com.fiuba.campus2015.extras.Constants.GROUP;
import static com.fiuba.campus2015.extras.Constants.GROUPOWNER;
import static com.fiuba.campus2015.extras.Constants.USER;


public class GroupContactFragment extends ContactFragment {
    private ListView listViewGroupContacts;
    private ContactsAdapter contactsAdapter;
    private RecyclerView recyclerView;
    private View myView;
    private String groupId;
    private ProgressBar prgrsBar;
    private TextView emptyView;
    private SessionManager session;
    private SearchFilter searchFilter;

    public static GroupContactFragment newInstance(Group group) {
        GroupContactFragment fragment = new GroupContactFragment();
        Bundle args = new Bundle();
        args.putString(GROUP, group._id);
        args.putString(GROUPOWNER, group.owner._id);
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.group_contact_fragment, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        groupId = getArguments().getString(GROUP);



        prgrsBar = (ProgressBar) myView.findViewById(R.id.progressBarGroupContacts);
        emptyView = (TextView) myView.findViewById(R.id.empty_contact_group_view);


        recyclerView = (RecyclerView) myView.findViewById(R.id.listViewGroupContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        contactsAdapter = new ContactsAdapter(getActivity(), this);

        contactsAdapter.setReadOnly();

        recyclerView.setAdapter(contactsAdapter);

        contactsAdapter.setContacts(new ArrayList<User>(), getArguments().getString(GROUPOWNER));

        searchFilter = new SearchFilter(getActivity(), this);

        getMembers();

        return myView;
    }



    //Se llama a este metodo en caso de que no haya error
    @Subscribe
    public void onMembersResponse(ArrayList<User> users) {

        if (!users.isEmpty()) {
            emptyView.setVisibility(View.INVISIBLE);
            contactsAdapter.setContacts(users,getArguments().getString(GROUPOWNER));
        }
        else
            emptyView.setVisibility(View.VISIBLE);

        prgrsBar.setVisibility(View.GONE);
        //Desuscripcion a los eventos que devuelve el cliente que llama la api
        Application.getEventBus().unregister(this);
    }


    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onResponse(com.fiuba.campus2015.services.Response responseError) {
        //TODO Mostrar errores en caso de error del request
        prgrsBar.setVisibility(View.GONE);
        Application.getEventBus().unregister(this);

    }

    public void getMembers() {
        prgrsBar.setVisibility(View.VISIBLE);
        Application.getEventBus().register(this);

        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<List<User>, IApiUser>() {
            @Override
            public List<User> getResult(IApiUser service) {
                return service.getMembersGroup(session.getToken(), groupId);
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<List<User>, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new com.fiuba.campus2015.services.Response());
    }

}
