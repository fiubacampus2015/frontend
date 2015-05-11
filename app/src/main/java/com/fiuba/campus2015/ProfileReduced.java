package com.fiuba.campus2015;


import android.os.AsyncTask;
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
import static com.fiuba.campus2015.extras.Constants.*;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.fragments.IProfile;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;
import retrofit.client.Response;


public class ProfileReduced extends ActionBarActivity implements IProfile {
    private Toolbar toolbar;
    private ImageView photo;
    private TextView name;
    private TextView email;
    private TextView fieldTxt2;
    private TextView fieldTxt3;
    private TextView fieldTxt4;
    private TextView fieldTxt5;
    private TextView fieldTxt6;
    private String _id;
    private ImageView icon2;
    private ImageView icon3;
    private ImageView icon4;
    private ImageView icon5;
    private ImageView icon6;
    private int position;
    private SessionManager session;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_reduced);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.addFriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendRequestTask task = new FriendRequestTask();
                task.execute();
            }
        });

        initialize();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String userJson = extras.getString(USER);
            User user = new Gson().fromJson(userJson, User.class);
            setUser(user);
        }

    }

    private void initialize() {
        position = 2;
        photo = (ImageView) findViewById(R.id.imageViewR);
        name = (TextView) findViewById(R.id.nombre_ProfileR);
        email = (TextView) findViewById(R.id.emailProfileR);
        fieldTxt2 = (TextView) findViewById(R.id.txtfield2);
        fieldTxt3 = (TextView) findViewById(R.id.txtfield3);
        fieldTxt4 = (TextView) findViewById(R.id.txtfield4);
        fieldTxt5 = (TextView) findViewById(R.id.txtfield5);
        fieldTxt6 = (TextView) findViewById(R.id.txtfield6);

        icon2 = (ImageView)findViewById(R.id.icon_field2);
        icon3 = (ImageView)findViewById(R.id.icon_field3);
        icon4 = (ImageView)findViewById(R.id.icon_field4);
        icon5 = (ImageView) findViewById(R.id.icon_field5);
        icon6 = (ImageView) findViewById(R.id.icon_field6);

        icon2.setVisibility(View.INVISIBLE);
        icon3.setVisibility(View.INVISIBLE);
        icon4.setVisibility(View.INVISIBLE);
        icon5.setVisibility(View.INVISIBLE);
        icon6.setVisibility(View.INVISIBLE);
    }

    private void loadData(User user) {
        getSupportActionBar().setTitle(user.name + " " + user.username);
        name.setText(user.name + " " + user.username);
        email.setText(user.email);

        String phoneText = user.personal.phones.mobile;
        if(phoneText != null && !phoneText.isEmpty()) {
            loadField(getResource(PHONE), phoneText);
        }

        if(user.education != null && !user.education.careers.isEmpty()) {
            String profesionText = user.education.careers.get(0).title;
            if (profesionText != null && !profesionText.isEmpty()) {
                loadField(getResource(PROFESION), profesionText);
            }
        }

        String genderText = user.personal.gender;
        if(genderText != null && !genderText.isEmpty()) {
            loadField(getResource(GENDER), genderText);
        }

        String birthdayText = Utils.getBirthdayFormatted(user.personal.birthday);
        if(birthdayText != null && !birthdayText.isEmpty()) {
            loadField(getResource(BIRTHDAY), birthdayText);
        }

        String nacionalityText = user.personal.nacionality;
        if(nacionalityText != null && !nacionalityText.isEmpty()) {
            loadField(getResource(NATIONALITY), nacionalityText);
        }

        String path = user.personal.photo;
        if(path != null && !path.isEmpty()) {
            photo.setImageBitmap(Utils.getPhoto(user.personal.photo));
        }

        _id = user._id;

        if (user.status != null && user.status.equals("pending"))
        {
            findViewById(R.id.addFriend).setVisibility(View.GONE);
        }

    }

    private void loadField(int src, String txt) {
        position++;
        switch (position - 1) {
            case(2):
                icon2.setImageResource(src);
                fieldTxt2.setText(txt);
                icon2.setVisibility(View.VISIBLE);
                break;
            case(3):
                icon3.setImageResource(src);
                fieldTxt3.setText(txt);
                icon3.setVisibility(View.VISIBLE);
                break;
            case(4):
                icon4.setImageResource(src);
                fieldTxt4.setText(txt);
                icon4.setVisibility(View.VISIBLE);
                break;
            case(5):
                icon5.setImageResource(src);
                fieldTxt5.setText(txt);
                icon5.setVisibility(View.VISIBLE);
                break;
            case(6):
                icon6.setImageResource(src);
                fieldTxt6.setText(txt);
                icon6.setVisibility(View.VISIBLE);
                break;
        }
    }

    private int getResource(String value) {
        switch (value) {
            case(PROFESION): return R.drawable.ic_school_grey600_24dp;
            case(PHONE): return R.drawable.ic_phone_iphone_grey600_24dp;
            case(GENDER): return R.drawable.ic_settings_grey;
            case(BIRTHDAY): return R.drawable.ic_cake_grey600_24dp;
            default: return R.drawable.ic_public_grey600_24dp;
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

    private  class FriendRequestTask extends AsyncTask<Void, Void, Response> {

        RestClient restClient;

        public FriendRequestTask() {
        }

        public void executeTask() {
            try {
                this.execute();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            restClient = new RestClient();
            //prgrsBar.setEnabled(true);
            //prgrsBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Response doInBackground(Void... params) {
            Response response = null;
            try {

                response = restClient.getApiService().invite(session.getToken(), session.getUserid(),_id);

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Hubo un error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();

            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            if (response == null) {
                Toast.makeText(getApplicationContext(), "Hubo un error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();
            } else {
                //prgrsBar.setVisibility(View.INVISIBLE);
                //contactsAdapter.setContacts(user);
                findViewById(R.id.addFriend).setEnabled(false);
            }
        }
    }

}


