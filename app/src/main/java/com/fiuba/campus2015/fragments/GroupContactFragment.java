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
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

import static com.fiuba.campus2015.extras.Constants.USER;


public class GroupContactFragment extends ContactFragment {
    private ListView listViewGroupContacts;
    private ContactsAdapter contactsAdapter;
    private RecyclerView recyclerView;
    private View myView;
    private EditText searchText;
    private ProgressBar prgrsBar;
    private TextView emptyView;
    private SessionManager session;
    private SearchFilter searchFilter;

    public static GroupContactFragment newInstance(String param1, String param2) {
        GroupContactFragment fragment = new GroupContactFragment();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.group_contact_fragment, container, false);

        session = new SessionManager(getActivity().getApplicationContext());


        ImageView buttonSearch = (ImageView) myView.findViewById(R.id.searchGroupContact);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers(false);
            }
        });

        ImageView buttonClear= (ImageView) myView.findViewById(R.id.search_group_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClear(v);
            }
        });


        prgrsBar = (ProgressBar) myView.findViewById(R.id.progressBarGroupContacts);
        emptyView = (TextView) myView.findViewById(R.id.empty_contact_group_view);


        recyclerView = (RecyclerView) myView.findViewById(R.id.listViewGroupContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        contactsAdapter = new ContactsAdapter(getActivity(), this);

        recyclerView.setAdapter(contactsAdapter);

        contactsAdapter.setContacts(new ArrayList<User>(),session.getUserid());
        searchUsers(true);
        //Se agrega esto por que sino no funciona el input con tabs
        searchText = (EditText) myView.findViewById(R.id.search_group_name);
        searchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchText.requestFocusFromTouch();
                return false;
            }
        });

        searchFilter = new SearchFilter(getActivity(), this);

        return myView;
    }

    public void removeGroupContact(User contact) {
        // Eliminar ocntacto del grupo
    }

    public void searchGroupUsers(boolean friend) {
        SearchGroupContacts task = new SearchGroupContacts(friend);
        try {
            task.execute();
        } catch (Exception x){
            Toast.makeText(getActivity().getApplicationContext(),"Error al buscar contactos.",Toast.LENGTH_SHORT).show();
        }

    }

    public void searchGroupClear(View view) {
        emptyView.setVisibility(view.INVISIBLE);
        searchText.setText("");
        searchFilter.reset();
    }

    private class DeleteGroupContact extends AsyncTask<Void, Void, Response> {
        RestClient restClient;
        private String userId;
        private Context context;

        public DeleteGroupContact(GroupContactFragment fragment, String userId) {
            this.context = fragment.getActivity();
            this.userId = userId;
        }

        public void executeTask() {
            try {
                this.execute();
            } catch (Exception e) {
                Toast.makeText(context.getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            restClient = new RestClient();

        }

        @Override
        protected Response doInBackground(Void... params) {
            Response response = null;
            try {
                response = restClient.getApiService().deleteFriend(session.getToken(), session.getUserid(), userId);
            }catch (Exception ex) {
            }

            return response;
        }

        @Override
        public void onPostExecute(Response response) {
            if (response== null)
                Toast.makeText(context.getApplicationContext(), "Hubo un error al eliminar el contacto.", Toast.LENGTH_SHORT).show();

        }
    }

    private  class SearchGroupContacts extends AsyncTask<Void, Void, List<User>> {

        RestClient restClient;
        Boolean searchFriend;

        public SearchGroupContacts(boolean searchFriend) {
            this.searchFriend = searchFriend;
        }

        public void executeTask() {
            try {
                this.execute();
            } catch (Exception e) {
                 Toast.makeText(getActivity().getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            restClient = new RestClient();
            prgrsBar.setEnabled(true);
            prgrsBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<User> doInBackground(Void... params) {
            List<User> user = null;
            try {

            } catch (Exception ex) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();

            }
            return user;
        }

        @Override
        protected void onPostExecute(List<User> user) {
            if (user == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();
            } else {
                if(user.isEmpty())
                    emptyView.setVisibility(View.VISIBLE);
                else
                    emptyView.setVisibility(View.INVISIBLE);

                prgrsBar.setVisibility(View.INVISIBLE);
                contactsAdapter.setContacts(user,session.getUserid());
            }
        }
    }
}
