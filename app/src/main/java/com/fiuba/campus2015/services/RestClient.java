package com.fiuba.campus2015.services;


import com.fiuba.campus2015.extras.UrlEndpoints;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient
{
    private IApiUser apiService;

    public RestClient()
    {
           Gson gson = new GsonBuilder()
                 .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                 .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(UrlEndpoints.URL_API)
                .setConverter(new GsonConverter(gson))
                .build();

        apiService = restAdapter.create(IApiUser.class);
    }

    public IApiUser getApiService()
    {
        return apiService;
    }
}