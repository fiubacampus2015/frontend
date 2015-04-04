package com.fiuba.campus2015;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.services.Response;

import retrofit.RestAdapter;

public class LoadPersonalData extends ActionBarActivity {
    private ImageView photo;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadpersonaldata_layout);

        photoShow();
        greetingShow();

        Button load = (Button)findViewById(R.id.buttonloadData);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPersonalDataTask task = new LoadPersonalDataTask();
                task.execute();
            }
        });

    }

    private void photoShow() {
        photo = (ImageView)findViewById(R.id.idPhoto);
        // se muestra la foto del usuario, por ahora se muestra la imagen por defecto
    }

    private void greetingShow() {
        greeting = (TextView)findViewById(R.id.idhellouser);

        // se muestra el nombre del usuario con el saludo
        String name = "";

        greeting.setText("Hola, " + name + "!");
    }

    private class LoadPersonalDataTask extends AsyncTask<Void, Void,
            Response> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://fiubacampus-staging.herokuapp.com")
                    .build();
        }

        @Override
        protected Response doInBackground(Void... params) {
            // se requiere el id y token para cargar los datos
           return  null;
        }

        @Override
        protected void onPostExecute(Response response) {

        }
    }
}
