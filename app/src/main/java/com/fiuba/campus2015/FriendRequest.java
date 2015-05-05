package com.fiuba.campus2015;


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
import com.gc.materialdesign.widgets.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

public class FriendRequest extends ActionBarActivity {
    private Toolbar toolbar;
    private RecyclerView list;
    private ContactRequestAdapter contactRequestAdapter;
    private EditText searchText;

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

        // probando...
        setInvitations();
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

    private void setInvitations() {
     //   ProgressDialog progressDialog = new ProgressDialog(this,"Cargando solicitudes");
       // progressDialog.show();

        List<User> users = new ArrayList<>();
        users.add(new User("michael", "fox", "", "michael@gmail.com", null));
        users.add(new User("agostina", "Donohue", "", "agostina@gmail.com", null));
        users.add(new User("adriana","ramirez","", "adriana@gmail.com",null));
        users.add(new User("cristina","fernandez","", "cristina@gmail.com",null));

        contactRequestAdapter.setContacts(users);

//        progressDialog.dismiss();
    }

    public void setUserInvited(User user) {
        Toast.makeText(this, "Se acepto la amistad de " + user.email,Toast.LENGTH_SHORT).show();
    }

    public void setInvitationDeleted(User user) {
        Toast.makeText(this, "Se borro la invitacion de " + user.email ,Toast.LENGTH_SHORT).show();
    }
}
