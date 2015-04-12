package com.fiuba.campus2015;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fiuba.campus2015.dto.user.Authenticate;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;
import com.fiuba.campus2015.session.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import retrofit.RestAdapter;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user  =  ((MaterialEditText)findViewById(R.id.textuser)).getText().toString();
                String password =  ((MaterialEditText)findViewById(R.id.textpassword)).getText().toString();

                if (user.isEmpty()) {
                    ((MaterialEditText) findViewById(R.id.textuser)).validateWith(new RegexpValidator("Ingresá tu usuario.", "\\d+"));
                }
                if (password.isEmpty()) {
                    ((MaterialEditText) findViewById(R.id.textpassword)).validateWith(new RegexpValidator("Ingresá tu contraseña.", "\\d+"));
                }

                if(!user.isEmpty() && !password.isEmpty())
                {
                    if (checkUser(user)) {
                        AuthenticateTask task = new AuthenticateTask();
                        try {
                            task.execute();
                        } catch (Exception x){
                            Toast.makeText(getApplicationContext(),"Credenciales incorrectas.",Toast.LENGTH_SHORT).show();
                        }
                    }
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

    private boolean checkUser(String user) {

        if (!user.contains("@")) {
            ((MaterialEditText) findViewById(R.id.textuser)).validateWith(new RegexpValidator("No es un email válido.", "\\d+"));
            return false;
        }
        /*else {
 TODO: descomentar cuando nos registremos con fiuba.
            if (!user.contains("fi.uba.ar")) {
                    ((MaterialEditText) findViewById(R.id.textuser)).validateWith(new RegexpValidator("Sólo se permiten emails de @fi.uba.ar.", "\\d+"));
                    return false;
                }
            }*/
        return true;
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
        if (id == R.id.submit_area) {
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
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
        }

        @Override
        protected Response doInBackground(Void... params) {

            String user  =  ((EditText)findViewById(R.id.textuser)).getText().toString();
            String password =  ((EditText)findViewById(R.id.textpassword)).getText().toString();

            IApiUser api = restAdapter.create(IApiUser.class);

            Response response = null;
            try {
                response = api.authenticate(new Authenticate(user, password));
            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            if(response == null)
                Toast.makeText(getApplicationContext(),"Credenciales incorrectas.",Toast.LENGTH_SHORT).show();
            else {
                if (response.token != null) {
                    SessionManager session;
                    session = new SessionManager(getApplicationContext());

                    String user  =  ((EditText)findViewById(R.id.textuser)).getText().toString();
                    session.createLoginSession(user,response.token ,response.id,response.name, response.surname);

                    Intent intent = new Intent(MainActivity.this, Board.class);
                    startActivity(intent);

                }
            }
        }
    }
}
