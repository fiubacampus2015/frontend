package com.fiuba.campus2015;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fiuba.campus2015.adapter.ContactRequestAdapter;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

public class FriendRequest extends ActionBarActivity {
    private Toolbar toolbar;
    private RecyclerView list;
    private ContactRequestAdapter contactRequestAdapter;
    private EditText searchText;
    private SessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchText = (EditText)findViewById(R.id.search_text_request);
        setListeners();

        list = (RecyclerView) findViewById(R.id.listFriendRequest);
        list.setLayoutManager(new LinearLayoutManager(this));

        contactRequestAdapter = new ContactRequestAdapter(this);
        list.setAdapter(contactRequestAdapter);

        session = new SessionManager(this);


        FriendRequestTask friendRequestTask = new FriendRequestTask(this);
        friendRequestTask.executeTask();
    }

    private void setListeners() {
        searchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchText.requestFocusFromTouch();
                return false;
            }
        });

        ImageView buttonSearch= (ImageView)findViewById(R.id.search_request);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers();
            }
        });

        ImageView buttonClear = (ImageView) findViewById(R.id.search_clear_request);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClear(v);
            }
        });
    }

    public void searchClear(View view) {
        searchText.setText("");
    }

    private void searchUsers() {
        Toast.makeText(this, "buscando: " + searchText.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private void setInvitations(List<User> users) {
        if(users == null) {
            Toast.makeText(this, "Hubo un error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();
            return;

        }

        contactRequestAdapter.setContacts(users);
    }

    public void setUserInvited(User user) {
        Toast.makeText(this, "Se acepto la amistad de " + user.email,Toast.LENGTH_SHORT).show();
    }

    public void setInvitationDeleted(User user) {
        Toast.makeText(this, "Se borro la invitacion de " + user.email ,Toast.LENGTH_SHORT).show();
    }




    private class FriendRequestTask extends AsyncTask<Void, Void, List<User>> {
        RestClient restClient;
        private FriendRequest friendRequest;
        private ProgressDialog progressDialog;
        private List<User> users;

        public FriendRequestTask(FriendRequest fr) {
            friendRequest = fr;
        }

        @Override
        protected void onPreExecute() {
            restClient = new RestClient();
            progressDialog = new ProgressDialog(friendRequest,"Cargando solicitudes");
            progressDialog.show();
        }

        private void executeTask() {
            try {
                this.execute();
            } catch(Exception e) {
                friendRequest.setInvitations(null);
            }
        }

        @Override
        protected List<User> doInBackground(Void... params) {
            try {
                users = restClient.getApiService().getInvitationsPending(session.getToken(),session.getUserid());
            } catch(Exception e) {
                friendRequest.setInvitations(null);
            }

            return users;
        }


        @Override
        protected void onPostExecute(List<User> listUsers) {
            progressDialog.dismiss();
            friendRequest.setInvitations(listUsers);
        }
    }
}

