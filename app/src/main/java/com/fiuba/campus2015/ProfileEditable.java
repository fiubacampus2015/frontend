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
import static com.fiuba.campus2015.extras.Constants.DESCRIPCIONEMPLEO;
import static com.fiuba.campus2015.extras.Constants.FECHAINGRESO;
import static com.fiuba.campus2015.extras.Constants.FECHAINGRESOEMPLEO;
import static com.fiuba.campus2015.extras.Constants.FECHASALIDAIEMPLEO;
import static com.fiuba.campus2015.extras.Constants.FORMAT_DATETIME;
import static com.fiuba.campus2015.extras.Constants.GENDER;
import static com.fiuba.campus2015.extras.Constants.LASTNAME;
import static com.fiuba.campus2015.extras.Constants.NAME;
import static com.fiuba.campus2015.extras.Constants.NATIONALITY;
import static com.fiuba.campus2015.extras.Constants.ORIENTATION;
import static com.fiuba.campus2015.extras.Constants.PHONE;
import static com.fiuba.campus2015.extras.Constants.PHOTO;
import static com.fiuba.campus2015.extras.Constants.PROFESION;
import static com.fiuba.campus2015.extras.Constants.USER;
import static com.fiuba.campus2015.extras.Constants.PAGE;
import static com.fiuba.campus2015.extras.Utils.stringToCalendar;

import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.fiuba.campus2015.adapter.PageAdapter;
import com.fiuba.campus2015.dto.user.Education;
import com.fiuba.campus2015.dto.user.Job;
import com.fiuba.campus2015.dto.user.Personal;
import com.fiuba.campus2015.dto.user.Phone;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.Dialog;
import com.google.gson.Gson;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setOffscreenPageLimit(3);

        // recibe de Profile el objero 'User' dentro de un Json
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String userJson = extras.getString(USER);
            User user = new Gson().fromJson(userJson, User.class);
            adapterViewPager = new PageAdapter(getSupportFragmentManager(), user);
            vpPager.setAdapter(adapterViewPager);
            vpPager.setCurrentItem(extras.getInt(PAGE));
        }
    }

    public void submitData(){
        Bundle data = adapterViewPager.getAllData();
        int error = data.getInt("ERROR");

        if (error != 999){
            vpPager.setCurrentItem(error);
        } else {
            if (!isFutureDate(data.getString(BIRTHDAY), data.getString(FECHAINGRESOEMPLEO))) {
                Dialog dialog = new Dialog(this, "Las fechas son incorrectas",
                        "La fecha de nacimiento no puede ser mayor que la fecha de ingreso al trabajo.");
                dialog.show();
            } else if (!isFutureDate(data.getString(BIRTHDAY), data.getString(FECHAINGRESO))) {
                    Dialog dialog = new Dialog(this, "Las fechas son incorrectas" ,
                            "La fecha de nacimiento no puede ser mayor a la fecha de ingreso a la facultad.");
                    dialog.show();
            } else {
                ExecuteSave(data);
            }
        }
    }

    private boolean isFutureDate(String pivotDate, String futureDate) {

        if (pivotDate != null && futureDate != null && !futureDate.isEmpty() && !pivotDate.isEmpty()) {
            try {
                return (stringToCalendar(futureDate).compareTo(stringToCalendar(pivotDate)) == 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    private void ExecuteSave(Bundle data){
        SaveUserDataTask saveUserDataTask = new SaveUserDataTask(this, data);
        saveUserDataTask.executeTask();
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(this,null, "Cambios no guardados");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBackPressed();
            }
        });
        dialog.addCancelButton("Guardar");
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
        dialog.show();
        dialog.getButtonAccept().setText("Descartar");
    }

    private void myBackPressed() {
        super.onBackPressed();
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
                Personal personal = new Personal(data.getString(PHOTO), data.getString(COMENTARIO),data.getString(NATIONALITY),"",data.getString(BIRTHDAY),data.getString(GENDER),phones);

                Education education = new Education();
                education.addCareer(data.getString(PROFESION),data.getString(ORIENTATION),data.getString(FECHAINGRESO));

                Job empleo = new Job();
                empleo.addCompany(data.getString(DESCRIPCIONEMPLEO),data.getString(FECHAINGRESOEMPLEO),data.getString(FECHASALIDAIEMPLEO));

                User user = new User(data.getString(NAME),data.getString(LASTNAME),personal,education,empleo);

                response = api.put(session.getToken(),session.getUserid(),user);

            } catch (Exception x) {

                Toast.makeText(editProfile.getApplicationContext(), x.toString(), Toast.LENGTH_SHORT).show();
            }

            return response;
        }

        @Override
        protected void onPostExecute(retrofit.client.Response response) {

            if(response == null) {
                Toast.makeText(editProfile.getApplicationContext(), "Hubo un error al guardar tus datos.", Toast.LENGTH_SHORT).show();
            } else {
                editProfile.back();
            }
        }
    }



}
