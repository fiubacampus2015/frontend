package com.fiuba.campus2015;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import static com.fiuba.campus2015.extras.Constants.BIRTHDAY;
import static com.fiuba.campus2015.extras.Constants.COMENTARIO;
import static com.fiuba.campus2015.extras.Constants.FECHAINGRESO;
import static com.fiuba.campus2015.extras.Constants.GENDER;
import static com.fiuba.campus2015.extras.Constants.LASTNAME;
import static com.fiuba.campus2015.extras.Constants.NAME;
import static com.fiuba.campus2015.extras.Constants.NATIONALITY;
import static com.fiuba.campus2015.extras.Constants.ORIENTATION;
import static com.fiuba.campus2015.extras.Constants.PHONE;
import static com.fiuba.campus2015.extras.Constants.PHOTO;
import static com.fiuba.campus2015.extras.Constants.PROFESION;
import static com.fiuba.campus2015.extras.Constants.USER;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.fiuba.campus2015.adapter.PageAdapter;
import com.fiuba.campus2015.dto.user.Education;
import com.fiuba.campus2015.dto.user.Personal;
import com.fiuba.campus2015.dto.user.Phone;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;
import retrofit.RestAdapter;
import retrofit.client.Response;


public class ProfileEditable extends ActionBarActivity {
    private Toolbar toolbar;
    private PageAdapter adapterViewPager;
    private ViewPager vpPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setOffscreenPageLimit(3);

        // recibe de Profile el objero 'User' dentro de un Json
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String userJson = extras.getString(USER);
            User user = new Gson().fromJson(userJson, User.class);
            adapterViewPager = new PageAdapter(getSupportFragmentManager(), user);
            vpPager.setAdapter(adapterViewPager);
        }
    }

    public void submitData(){
        Bundle data = adapterViewPager.getAllData();
        SaveUserDataTask saveUserDataTask = new SaveUserDataTask(this, data);
        saveUserDataTask.executeTask();
    }

    public void back() {
        Toast.makeText(getApplicationContext(), "Los datos se guardaron correctamente", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSubmit = menu.findItem(R.id.action_submit);
        itemSubmit.setVisible(true);
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
        switch (item.getItemId()) {
            case R.id.action_submit:
                submitData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }







    private class SaveUserDataTask extends AsyncTask<Void, Void, Response> {
        private RestAdapter restAdapter;
        private ProfileEditable editProfile;
        private Bundle data;

        public SaveUserDataTask(ProfileEditable editProfile, Bundle data) {
            this.editProfile = editProfile;
            this.data = data;
        }

        public void executeTask() {
            try {
                this.execute();
            } catch (Exception e) {
                Toast.makeText(editProfile.getApplicationContext(),"Error.",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
        }

        @Override
        protected retrofit.client.Response doInBackground(Void... params) {
            IApiUser api = restAdapter.create(IApiUser.class);
            retrofit.client.Response  response = null;

            try {
                SessionManager session = new SessionManager(editProfile.getApplicationContext());

                Phone phones = new Phone(data.getString(PHONE),"");
                Education education = new Education();
                education.addCareer(data.getString(PROFESION),data.getString(ORIENTATION),data.getString(FECHAINGRESO));

                Personal personal = new Personal(data.getString(PHOTO), data.getString(COMENTARIO),data.getString(NATIONALITY),"",data.getString(BIRTHDAY),data.getString(GENDER),phones);
                User user = new User(data.getString(NAME),data.getString(LASTNAME),personal,education);

                response = api.put(session.getToken(),session.getUserid(),user);

            } catch (Exception x) {

                Toast.makeText(editProfile.getApplicationContext(), x.toString(), Toast.LENGTH_SHORT).show();
            }

            return response;
        }

        @Override
        protected void onPostExecute(retrofit.client.Response response) {

            if(response == null) {
                Toast.makeText(editProfile.getApplicationContext(), "Hubo un error al guardar los datos del alumno.", Toast.LENGTH_SHORT).show();
            } else {
                editProfile.back();
            }
        }
    }



}