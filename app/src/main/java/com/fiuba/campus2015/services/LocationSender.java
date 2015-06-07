package com.fiuba.campus2015.services;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.fiuba.campus2015.dto.user.Position;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationSender implements
        GoogleApiClient.ConnectionCallbacks
{
    private GoogleApiClient mGoogleApiClient;
    private Context context;


    public void send(Context context,final String token, final String user)
    {

        this.context = context;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
            final double currentLatitude = location.getLatitude();
            final double currentLongitude = location.getLongitude();


            PoolingService.GetResult result = new PoolingService.GetResult<retrofit.client.Response, IApiUser>() {
                @Override
                public retrofit.client.Response getResult(IApiUser service) {
                    return service.sendLocation(token, user, new Position(String.valueOf(currentLatitude) + String.valueOf(currentLongitude)));
                }
            };

            RestClient restClient = new RestClient();

            PoolingService callApi = new PoolingService<retrofit.client.Response, IApiUser>();
            callApi.fetch(restClient.getApiService(), result, new com.fiuba.campus2015.services.Response());
        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        // Create the LocationRequest object
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
