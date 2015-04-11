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

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.PageAdapter;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;

import retrofit.RestAdapter;
import retrofit.client.Response;

/**
 * Created by ismael on 09/04/15.
 */
public class LoadFragment extends Fragment{
    private PageAdapter adapterViewPager;
    private ViewPager vpPager;
    private SessionManager session;
    private View myView;
    private User userdata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        myView =inflater.inflate(R.layout.load, container, false);

        UserTask task = new UserTask();
        try {
            task.execute();

        } catch (Exception x){
            Toast.makeText(getActivity().getApplicationContext(),"Error.",Toast.LENGTH_SHORT).show();
        }


        return myView;
    }

    public void submitData(){

        Toast.makeText(getActivity().getApplicationContext(), "Se tiene que guardar", Toast.LENGTH_SHORT).show();

    }


    // necesario para devolver la foto elegida al fragment hijo
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class UserTask extends AsyncTask<Void, Void,
            User> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
        }

        @Override
        protected User doInBackground(Void... params) {

            IApiUser api = restAdapter.create(IApiUser.class);
            User  user = null;
            try {
                session = new SessionManager(getActivity().getApplicationContext());

                user = api.get(session.getToken(),session.getUserid());

            } catch (Exception x) {}

            //adapterViewPager.setDataUser(new User(user.name,user.username,user.email,user.email));

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

            }
        }
    }
}
