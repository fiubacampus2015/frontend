package com.fiuba.campus2015;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;

import retrofit.RestAdapter;

/**
 * Created by gonzalovelasco on 29/3/15.
 */
public class Registration extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        Button singup = (Button) findViewById(R.id.registration);
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, MainActivity.class);
                startActivity(intent);

                RegisterTask task = new RegisterTask();
                task.execute();
            }
        });

    }

    private class RegisterTask extends AsyncTask<Void, Void,
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

            String user  =  ((EditText)findViewById(R.id.editName)).getText().toString();
            String password =  ((EditText)findViewById(R.id.editPassword)).getText().toString();
            String email =  ((EditText)findViewById(R.id.editEmail)).getText().toString();

            IApiUser api = restAdapter.create(IApiUser.class);
            Response response = api.register(new User(user, user, password, email));

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            Intent intent = new Intent(Registration.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
