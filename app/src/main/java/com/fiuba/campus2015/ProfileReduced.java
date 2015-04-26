package com.fiuba.campus2015;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.fiuba.campus2015.asyntask.LoadUserDataTask;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.fragments.IProfile;
import com.google.gson.Gson;

import static com.fiuba.campus2015.extras.Constants.TOKEN;
import static com.fiuba.campus2015.extras.Constants.USER;


public class ProfileReduced extends ActionBarActivity implements IProfile {
    private Toolbar toolbar;
    private ImageView photo;
    private TextView name;
    private TextView email;
    private TextView nationality;
    private TextView birthday;
    private TextView gender;
    private TextView phone;

    /*
    Cuando se llame a esta actividad pasarle a traves del intent el id y
    token del usuario que se desea consultar :/

     Intent intent = new Intent(this, ProfileReduced.class);
     intent.putExtra(USER, idUser);
     intent.putExtra(TOKEN, tokenUSer);
     startActivity(intent);
     */


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_reduced);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initialize();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String userJson = extras.getString(USER);
            User user = new Gson().fromJson(userJson, User.class);
            setUser(user);
             //LoadUserDataTask loadTask = new LoadUserDataTask(this, extras.getString(TOKEN), extras.getString(USER));
             //loadTask.executeTask();
        }


    }

    private void initialize() {
        photo = (ImageView) findViewById(R.id.imageViewR);
        name = (TextView) findViewById(R.id.nombre_ProfileR);
        email = (TextView) findViewById(R.id.emailProfileR);
        nationality = (TextView) findViewById(R.id.nacionalidaProfileR);
        birthday = (TextView) findViewById(R.id.cumpleProfileR);
        gender = (TextView) findViewById(R.id.generoProfileR);
        phone = (TextView) findViewById(R.id.celularProfileR);
    }

    private void loadData(User user) {
        getSupportActionBar().setTitle(user.name + " " + user.username);
        name.setText(user.name + " " + user.username);
        email.setText(user.email);
        phone.setText(user.personal.phones.mobile);
        nationality.setText(user.personal.nacionality);

        String path = user.personal.photo;
        if(path != null && !path.isEmpty()) {
            photo.setImageBitmap(Utils.getPhoto(user.personal.photo));
        }

        birthday.setText(Utils.getBirthdayFormatted(user.personal.birthday));

        String genero = user.personal.gender;
        if(genero != null && !genero.isEmpty()) {
            gender.setText(Utils.getGender(genero));
        }


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSubmit = menu.findItem(R.id.action_submit);
        itemSubmit.setVisible(false);
        MenuItem itemSearch = menu.findItem(R.id.action_edit);
        itemSearch.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUser(User user) {
        if(user == null) {
            Toast.makeText(this, "Hubo un error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }
        loadData(user);
    }
}
