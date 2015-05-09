package com.fiuba.campus2015.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.fiuba.campus2015.ProfileEditable;
import com.fiuba.campus2015.ProfileFriend;
import com.fiuba.campus2015.ProfileReduced;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.ContactItem;
import com.fiuba.campus2015.adapter.ContactsAdapter;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.RecyclerItemClickListener;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.fiuba.campus2015.extras.Constants.USER;


public class ContactFragment extends Fragment {
    private ListView listViewContacts;
    private ContactsAdapter contactsAdapter;
    private RecyclerView recyclerView;
    private View myView;
    private EditText searchText;
    private ProgressBar prgrsBar;
    private TextView emptyView;
    private SessionManager session;
    private SearchFilter searchFilter;

    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.contact_fragment, container, false);

        session = new SessionManager(getActivity().getApplicationContext());


        ImageView buttonSearch = (ImageView) myView.findViewById(R.id.search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers(false);
            }
        });

        ImageView buttonClear= (ImageView) myView.findViewById(R.id.search_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClear(v);
            }
        });


        prgrsBar = (ProgressBar) myView.findViewById(R.id.progressBarCircularIndeterminate);
        emptyView = (TextView) myView.findViewById(R.id.empty_view);


        recyclerView = (RecyclerView) myView.findViewById(R.id.listViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        contactsAdapter = new ContactsAdapter(getActivity());
        //addAllContacts(fillContacts());
        recyclerView.setAdapter(contactsAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        User contact = contactsAdapter.getContact(position);
                        Intent intent = new Intent(getActivity(), ProfileFriend.class);
                        //O perfil reducido
                        //Intent intent = new Intent(getActivity(), ProfileReduced.class);
                        intent.putExtra(USER, new Gson().toJson(contact));
                        startActivity(intent);
                    }
                })
        );



        contactsAdapter.setContacts(new ArrayList<User>(),session.getUserid());
        searchUsers(true);
        //Se agrega esto por que sino no funciona el input con tabs
        searchText = (EditText) myView.findViewById(R.id.search_text);
        searchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchText.requestFocusFromTouch();
                return false;
            }
        });

        ImageView buttonAdvancedSearch = (ImageView) myView.findViewById(R.id.advancedSearch);
        buttonAdvancedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });

        searchFilter = new SearchFilter(getActivity());

        return myView;
    }

    private void showSearchDialog() {
        searchFilter.showDialog();
    }

    public void searchUsers(boolean friend) {
        SearchUsers task = new SearchUsers(friend);
        try {
            task.execute();
        } catch (Exception x){
            Toast.makeText(getActivity().getApplicationContext(),"Error al buscar contactos.",Toast.LENGTH_SHORT).show();
        }

    }

    public void searchClear(View view) {
        emptyView.setVisibility(View.INVISIBLE);
        searchText.setText("");
        searchFilter.reset();
        searchUsers(true);
    }

    private  class SearchUsers extends AsyncTask<Void, Void, List<User>> {

        RestClient restClient;
        Boolean searchFriend;

        public SearchUsers(boolean searchFriend) {
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
                if (searchFriend)
                    //user = restClient.getApiService().getFriends(session.getToken(),session.getUserid(),"");
                    user = restClient.getApiService().getAll(session.getToken(), searchText.getText().toString(),session.getUserid(),true);
                else
                    user = restClient.getApiService().getFriend(session.getToken(), searchText.getText().toString(), true);

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
