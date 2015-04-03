package com.fiuba.campus2015;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fiuba.campus2015.dto.user.Authenticate;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;

import retrofit.RestAdapter;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user  =  ((EditText)findViewById(R.id.textuser)).getText().toString();
                String password =  ((EditText)findViewById(R.id.textpassword)).getText().toString();

                if (user.isEmpty() || password.isEmpty())
                    Toast.makeText(getApplicationContext(),"Usuario Incorrecto",Toast.LENGTH_SHORT).show();
                else
                {
                    AuthenticateTask task = new AuthenticateTask();
                    task.execute();

                }
            }
        });

        Button singup = (Button) findViewById(R.id.signUp);
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Registration.class);
                    startActivity(intent);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class AuthenticateTask extends AsyncTask<Void, Void,
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

            String user  =  ((EditText)findViewById(R.id.textuser)).getText().toString();
            String password =  ((EditText)findViewById(R.id.textpassword)).getText().toString();

            IApiUser api = restAdapter.create(IApiUser.class);
            Response response = api.authenticate(new Authenticate(user, password));

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            if(response.token != null) {
                Intent intent = new Intent(MainActivity.this, Board.class);
                startActivity(intent);
            }
        }
    }
}
