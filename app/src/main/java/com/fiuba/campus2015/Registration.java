package com.fiuba.campus2015;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;

import retrofit.RestAdapter;

/**
 * Created by gonzalovelasco on 29/3/15.
 */
public class Registration extends ActionBarActivity {

    private Toolbar toolbar;
    private TextView lastname;
    private TextView name;
    private TextView password;
    private TextView confirmPassword;
    private TextView email;
    private Spinner nationality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lastname = (TextView)findViewById(R.id.editLastName);
        name = (TextView) findViewById(R.id.editName);
        password = (TextView) findViewById(R.id.editPassword);
        confirmPassword = (TextView) findViewById(R.id.editConfirmPassword);
        email = (TextView) findViewById(R.id.editEmail);


        //Carga del combo desplegable para la nacionalidad
        nationality = (Spinner) findViewById(R.id.nationality);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationality.setAdapter(adapter);

        Button singup = (Button) findViewById(R.id.button);
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    RegisterTask task = new RegisterTask();
                    task.execute();
                }
            }
        });

    }

    public void onGenderRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.genderFemale:
                if (checked)
                    // female
                    break;
            case R.id.genderMale:
                if (checked)
                    // male
                    break;
        }
    }

    private boolean checkFields() {
        if(!isEmpty(lastname) && !isEmpty(name) && !isEmpty(password) &&
                !isEmpty(confirmPassword) && !isEmpty(email)) {
            String pass1 = password.getText().toString();
            String pass2 = confirmPassword.getText().toString();
            if(pass1.equals(pass2)) {
                return true;
            }
            else {
                Toast.makeText(getApplicationContext(),"Las constraseñas no coinciden",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),"Complete todos los campos",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isEmpty(TextView textview) {
        return (textview.getText().length() == 0);
    }


    private class RegisterTask extends AsyncTask<Void, Void,
            retrofit.client.Response> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://fiubacampus-staging.herokuapp.com")
                    .build();
        }

        @Override
        protected retrofit.client.Response doInBackground(Void... params) {
            String user  =  ((EditText)findViewById(R.id.editName)).getText().toString();
            String password =  ((EditText)findViewById(R.id.editPassword)).getText().toString();
            String email =  ((EditText)findViewById(R.id.editEmail)).getText().toString();

            IApiUser api = restAdapter.create(IApiUser.class);
            return  api.register(new User(user, user, password, email));
        }

        @Override
        protected void onPostExecute(retrofit.client.Response response) {
            if(response.getStatus() == 201) { //
                Toast.makeText(getApplicationContext(),"Tu registro fue exitoso",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Registration.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),"Hubo un error al registrarse.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
