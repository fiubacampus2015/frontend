package com.fiuba.campus2015;


import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.Dialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class ContactLocation extends ActionBarActivity {

    private Toolbar toolbar;
    private SessionManager session;
    private String content;

    public static final String TAG = Map.class.getSimpleName();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_location_layout);
        setUpMapIfNeeded();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getUsers();
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
    protected void onResume() {
        super.onResume();
        Application.getEventBus().register(this);
        setUpMapIfNeeded();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Application.getEventBus().unregister(this);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }


    private void setUpMap(List<User> users)
    {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        boolean markers = false;
        LatLng latLng = null;
        int minLat = Integer.MAX_VALUE;
        int maxLat = Integer.MIN_VALUE;
        int minLon = Integer.MAX_VALUE;
        int maxLon = Integer.MIN_VALUE;

        for (int i = 0; i < users.size(); i++)
        {
            String position = users.get(i).lastPosition;
            if (position!=null && !position.isEmpty())
            {
                String[] pos = position.split(":");
                double currentLatitude =  Double.parseDouble(pos[0]);
                double currentLongitude =  Double.parseDouble(pos[1]);

                latLng = new LatLng(currentLatitude, currentLongitude);
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(users.get(i).name+" " +users.get(i).username);
                mMap.addMarker(options).showInfoWindow();
                builder.include(options.getPosition());

                int lat = (int) (currentLatitude * 1E6);
                int lon = (int) (currentLongitude * 1E6);

                maxLat = Math.max(lat, maxLat);
                minLat = Math.min(lat, minLat);
                maxLon = Math.max(lon, maxLon);
                minLon = Math.min(lon, minLon);
                markers = true;

            }
        }


        if (markers) {
            LatLngBounds bounds = builder.build();
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu;
            if (users.size()>1) {
                //cu = CameraUpdateFactory.newLatLngBounds(bounds, 5);
                double latitudeToGo = (maxLat + minLat) / 1E6 / 2;
                double longitudeToGo = (maxLon + minLon) / 1E6 / 2;
                LatLng toCenter = new LatLng(latitudeToGo, longitudeToGo);

                //cu = CameraUpdateFactory.newLatLngZoom(toCenter, 20);
                //mMap.animateCamera(cu);
                LatLng southWestLatLon = new LatLng(minLat / 1E6, minLon / 1E6);
                LatLng northEastLatLon = new LatLng(maxLat / 1E6, maxLon / 1E6);

                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(southWestLatLon, northEastLatLon), 300));
            }
            else{
                cu = CameraUpdateFactory.newLatLngZoom(latLng,14);
                mMap.animateCamera(cu);
                }

        }else
        {
            final Dialog dialog2 = new Dialog(this, null, "No hay contactos para mostrar en el mapa.");
            dialog2.show();
            dialog2.getButtonAccept().setText("Aceptar");
            dialog2.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                    onBackPressed();
                }
            });

        }

    }

    //Se llama a este metodo en caso de que no haya error
    @Subscribe
    public void onUsersResponse(ArrayList<User> users) {

        //prgrsBar.setVisibility(View.GONE);
        setUpMap(users);
    }


    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onErrorResponse(Response responseError) {
        //TODO Mostrar errores en caso de error del request
        //prgrsBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Hubo un error" + responseError.reason, Toast.LENGTH_SHORT).show();

    }

    public void getUsers()
    {
        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<List<User>, IApiUser>() {
            @Override
            public List<User> getResult(IApiUser service) {
                return service.getFriends(session.getToken(), session.getUserid());
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<List<User>, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }

}