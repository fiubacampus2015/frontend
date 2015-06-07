package com.fiuba.campus2015.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.fiuba.campus2015.ContactLocation;
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
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.ProgressDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;

import retrofit.client.Response;

import static com.fiuba.campus2015.extras.Constants.PAGE;
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
        setHasOptionsMenu(true);

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


        contactsAdapter = new ContactsAdapter(getActivity(), this, session.getUserid());

        recyclerView.setAdapter(contactsAdapter);

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

        searchFilter = new SearchFilter(getActivity(), this);

        return myView;
    }

    public void loadProfile(User contact) {
        Intent intent;

        //Verifico si es amigo o no para mostrar el perfil
        if (contact.status != null && contact.status.equals("ok"))
        {
            intent = new Intent(getActivity(), ProfileFriend.class);

        }else
        {
            intent = new Intent(getActivity(), ProfileReduced.class);

        }
        intent.putExtra(USER, new Gson().toJson(contact));
        startActivity(intent);
    }

    public void removeContact(User contact) {
//        Toast.makeText(getActivity().getApplicationContext(),contact.name,Toast.LENGTH_SHORT).show();

        DeleteContact deleteContact = new DeleteContact(this, contact._id);
        deleteContact.executeTask();
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_location).setVisible(true);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_location:
                Intent intent = new Intent(getActivity(), ContactLocation.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
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

    private class DeleteContact extends AsyncTask<Void, Void, retrofit.client.Response> {
        RestClient restClient;
        private String userId;
        private Context context;

        public DeleteContact(ContactFragment contactFragment, String userId) {
            this.context = contactFragment.getActivity();
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
        public void onPostExecute(retrofit.client.Response response) {
            if (response== null)
                Toast.makeText(context.getApplicationContext(), "Hubo un error al eliminar el contacto.", Toast.LENGTH_SHORT).show();

        }
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
                    user = restClient.getApiService().getFriends(session.getToken(),session.getUserid());
                else {
                    if(searchFilter.filter()) {
                        user = restClient.getApiService().getPeople(session.getToken(),
                                searchFilter.getName(), searchFilter.getSurname(), searchFilter.getCareer(),
                                searchFilter.getOrientacion(), searchFilter.getNacionalidad());
                    } else {
                        user = restClient.getApiService().getPeople(session.getToken(),
                                searchText.getText().toString(), "", "", "", "");
                    }
                }
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
