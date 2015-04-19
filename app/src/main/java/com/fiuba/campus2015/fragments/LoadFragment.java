package com.fiuba.campus2015.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static com.fiuba.campus2015.dto.user.Personal.*;
import static com.fiuba.campus2015.extras.Constants.*;

import com.fiuba.campus2015.Board;
import com.fiuba.campus2015.Profile;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.PageAdapter;
import com.fiuba.campus2015.dto.user.Education;
import com.fiuba.campus2015.dto.user.Personal;
import com.fiuba.campus2015.dto.user.Phone;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by ismael on 09/04/15.
 */
public class LoadFragment extends Fragment{
    private PageAdapter adapterViewPager;
    private ViewPager vpPager;
    private SessionManager session;
    private View myView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        myView =inflater.inflate(R.layout.load, container, false);

        LoadUserDataTask task = new LoadUserDataTask();
        try {
            task.execute();

        } catch (Exception x){
            Toast.makeText(getActivity().getApplicationContext(),"Error.",Toast.LENGTH_SHORT).show();
        }


        return myView;
    }

    public void submitData(){


        SaveUserDataTask task = new SaveUserDataTask();
        try {
            task.execute();

        } catch (Exception x){
            Toast.makeText(getActivity().getApplicationContext(),"Error.",Toast.LENGTH_SHORT).show();
        }
    }


    // necesario para devolver la foto elegida al fragment hijo
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //TODO encapsular esto en una clase para que se llame en una sola instancia
    private class LoadUserDataTask extends AsyncTask<Void, Void,
            User> {
        RestAdapter restAdapter;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .setConverter(new GsonConverter(gson))
                    .build();
        }

        @Override
        protected User doInBackground(Void... params) {

            IApiUser api = restAdapter.create(IApiUser.class);
            User  user = null;
            try {
                session = new SessionManager(getActivity().getApplicationContext());

                user = api.get(session.getToken(),session.getUserid());

            } catch (Exception aaaa) {

                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();

            }

            return user;
        }

        @Override
        protected void onPostExecute(User user) {

            if(user == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();
            } else {

                adapterViewPager = new PageAdapter(getChildFragmentManager(),user);
                vpPager = (ViewPager) myView.findViewById(R.id.vpPager);
                vpPager.setAdapter(adapterViewPager);
                vpPager.setOffscreenPageLimit(3); // esto guarda el estado delos fragmentos no borrar!!!

            }
        }
    }


    //TODO encapsular esto en una clase para que se llame en una sola instancia
    private class SaveUserDataTask extends AsyncTask<Void, Void,
            retrofit.client.Response> {
        RestAdapter restAdapter;

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
                session = new SessionManager(getActivity().getApplicationContext());

                Bundle data = adapterViewPager.getAllData();
                Phone phones = new Phone(data.getString(PHONE),"");
                Education education = new Education();
                education.addCareer(data.getString(PROFESION),data.getString(ORIENTATION),data.getString(FECHAINGRESO));

                Personal personal = new Personal(data.getString(PHOTO), data.getString(COMENTARIO),data.getString(NATIONALITY),"",data.getString(BIRTHDAY),data.getString(GENDER),phones);
                User user = new User(data.getString(NAME),data.getString(LASTNAME),personal,education);

                response = api.put(session.getToken(),session.getUserid(),user);

            } catch (Exception x) {

                Toast.makeText(getActivity().getApplicationContext(), x.toString(), Toast.LENGTH_SHORT).show();
            }

            return response;
        }

        @Override
        protected void onPostExecute(retrofit.client.Response response) {

            if(response == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al guardar los datos del alumno.", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(getActivity().getApplicationContext(), "Los datos se guardaron correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Board.class);
                startActivity(intent);

            }
        }
    }
}
