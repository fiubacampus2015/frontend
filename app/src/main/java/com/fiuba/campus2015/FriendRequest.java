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
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.adapter.ContactRequestAdapter;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

public class FriendRequest extends ActionBarActivity {
    private Toolbar toolbar;
    private RecyclerView list;
    private ContactRequestAdapter contactRequestAdapter;
    private EditText searchText;
    private SessionManager session;
    private FriendRequestTask friendRequestTask;
    private InvitationTask invitationTask;
    private TextView emptyView;

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

        emptyView = (TextView) findViewById(R.id.empty_view);


        friendRequestTask = new FriendRequestTask(this);

        invitationTask = new InvitationTask();

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
        Toast.makeText(this, "Se acepto la solicitud de " + user.name,Toast.LENGTH_SHORT).show();
        invitationTask.setConfirmInvitation(user._id);
        invitationTask.executeTask();
        if(contactRequestAdapter.getItemCount() == 0)
            emptyView.setVisibility(View.VISIBLE);

        //friendRequestTask.executeTask();
    }

    public void setInvitationDeleted(User user) {
        Toast.makeText(this, "Se rechazo la invitacion de " + user.name ,Toast.LENGTH_SHORT).show();
        invitationTask.setRejectInvitation(user._id);
        invitationTask.executeTask();
        if (contactRequestAdapter.getItemCount() == 0)
            emptyView.setVisibility(View.VISIBLE);


        //friendRequestTask.executeTask();

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

            if(listUsers.isEmpty())
                emptyView.setVisibility(View.VISIBLE);
            else
                emptyView.setVisibility(View.INVISIBLE);

            progressDialog.dismiss();
            friendRequest.setInvitations(listUsers);
        }
    }

    private class InvitationTask extends AsyncTask<Void, Void, Response> {
        private RestClient restClient;
        private ProgressDialog progressDialog;
        private Boolean acceptInvitation;
        private String userId;


        public InvitationTask() {
        }

        private void setRejectInvitation(String userId) {
            this.acceptInvitation = false;
            this.userId = userId;
        }

        private void setConfirmInvitation(String userId) {
            this.acceptInvitation = true;
            this.userId = userId;
        }

        @Override
        protected void onPreExecute() {
            restClient = new RestClient();

        }

        private void executeTask() {
            try {
                this.execute();
            } catch(Exception e) {
            }
        }

        @Override
        protected Response doInBackground(Void... params) {
            Response response = null;
            try {

                if (this.acceptInvitation)
                    response = restClient.getApiService().confirmInvitation(session.getToken(),this.userId,session.getUserid());
                else
                    response = restClient.getApiService().rejectInvitation(session.getToken(), this.userId, session.getUserid());

            } catch(Exception e) {

            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {

            if (response == null)
                Toast.makeText(getApplicationContext(), "Hubo un error al rechazar o aceptar la invitacion.", Toast.LENGTH_SHORT).show();

        }
    }
}

