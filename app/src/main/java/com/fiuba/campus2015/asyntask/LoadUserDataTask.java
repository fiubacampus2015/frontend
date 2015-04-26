package com.fiuba.campus2015.asyntask;


import android.os.AsyncTask;

import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.fragments.IProfile;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class LoadUserDataTask extends AsyncTask<Void, Void, User> {
    private RestAdapter restAdapter;
    private Gson gson;
    private IProfile profile;
    private String tokenUser;
    private String idUser;

    public LoadUserDataTask(IProfile profile, String tokenUser, String idUser) {
        this.profile = profile;
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        this.tokenUser = tokenUser;
        this.idUser = idUser;
    }

    public void executeTask() {
        try {
            this.execute();
        } catch (Exception e) {
            profile.setUser(null);
        }
    }

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
        User user = null;
        try {
            user = api.get(tokenUser, idUser);

        } catch (Exception aaaa) {}
        return user;
    }

    @Override
    protected void onPostExecute(User user) {
      profile.setUser(user);
    }
}

