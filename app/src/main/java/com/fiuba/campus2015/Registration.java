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

import com.fiuba.campus2015.dto.user.Personal;
import com.fiuba.campus2015.dto.user.Phone;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.ProgressDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
    private String gender = "";
    private Dialog dialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lastname = (MaterialEditText)findViewById(R.id.editLastName);
        name = (MaterialEditText) findViewById(R.id.editName);
        password = (MaterialEditText) findViewById(R.id.editPassword);
        confirmPassword = (MaterialEditText) findViewById(R.id.confirmPassword);
        email = (MaterialEditText) findViewById(R.id.editEmail);


        //Carga del combo desplegable para la nacionalidad
        nationality = (Spinner) findViewById(R.id.nationality);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationality.setAdapter(adapter);

        ButtonFloatMaterial singup = (ButtonFloatMaterial) findViewById(R.id.registrate);
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields()) {
                    RegisterTask task = new RegisterTask();
                    task.execute();
                }
            }
        });
        progressDialog = new ProgressDialog(this, "Registrando");
    }

    public void onGenderRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.genderFemale:
                if (checked)
                    gender= "F";
                    break;
            case R.id.genderMale:
                if (checked)
                    gender= "M";
                    break;
        }
    }

    private boolean checkFields() {

        if(isEmpty(password)) ((MaterialEditText) findViewById(R.id.editPassword)).validateWith(new RegexpValidator("Ingresá una contraseña.", "\\d+"));
        if(isEmpty(confirmPassword)) ((MaterialEditText) findViewById(R.id.confirmPassword)).validateWith(new RegexpValidator("Confirmala.", "\\d+"));
        if(isEmpty(name)) ((MaterialEditText) findViewById(R.id.editName)).validateWith(new RegexpValidator("Ingresá tu nombre.", "\\d+"));
        if(isEmpty(lastname)) ((MaterialEditText) findViewById(R.id.editLastName)).validateWith(new RegexpValidator("Ingresá tu apellido.", "\\d+"));
        if(isEmpty(email)) ((MaterialEditText) findViewById(R.id.editEmail)).validateWith(new RegexpValidator("Ingresá tu email de fiuba.", "\\d+"));

        if(!isEmpty(lastname) && !isEmpty(name) && !isEmpty(password) &&
                !isEmpty(confirmPassword) && !isEmpty(email)) {

            //coincidencia de contraseñas
            String pass1 = password.getText().toString();
            String pass2 = confirmPassword.getText().toString();
            if (!pass1.equals(pass2)) {
                ((MaterialEditText) findViewById(R.id.editPassword)).validateWith(new RegexpValidator("", "\\d+"));
                ((MaterialEditText) findViewById(R.id.confirmPassword)).validateWith(new RegexpValidator("Las contraseñas no coinciden.", "\\d+"));

                return false;
            } else {
                if (pass1.length() < 8) {
                    ((MaterialEditText) findViewById(R.id.editPassword)).validateWith(new RegexpValidator("Fijate el mínimo.", "\\d+"));
                    ((MaterialEditText) findViewById(R.id.confirmPassword)).validateWith(new RegexpValidator("Fijate el mínimo.", "\\d+"));
                    return false;
                }
            }

            //mail ingresado valido
            String emailstring = email.getText().toString();
            if (emailstring.contains("@")) {
               /*  TODO: descomentar cuando nos registremos con fiuba.
               if (!emailstring.contains("fi.uba.ar")) {
                    ((MaterialEditText) findViewById(R.id.editEmail)).validateWith(new RegexpValidator("Sólo se permiten emails de @fi.uba.ar.", "\\d+"));
                    return false;
                }*/
            } else {
                ((MaterialEditText) findViewById(R.id.editEmail)).validateWith(new RegexpValidator("No es un email válido.", "\\d+"));
                return false;
            }

            return true;
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
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
            progressDialog.show();
        }

        @Override
        protected retrofit.client.Response doInBackground(Void... params) {
            String userName  =  ((EditText)findViewById(R.id.editName)).getText().toString();
            String userLastName  =  ((EditText)findViewById(R.id.editLastName)).getText().toString();
            String password =  ((EditText)findViewById(R.id.editPassword)).getText().toString();
            String email =  ((EditText)findViewById(R.id.editEmail)).getText().toString();
            String nationality =  ((Spinner)findViewById(R.id.nationality)).getSelectedItem().toString();
            String mobile =  ((EditText)findViewById(R.id.editPhone)).getText().toString();

            Phone phone = new Phone(mobile,"");
            Personal personal = new Personal(nationality,gender,phone);

            IApiUser api = restAdapter.create(IApiUser.class);
            retrofit.client.Response  response = null;
            try {
                response = api.register(new User(userName, userLastName, password, email, personal));

            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(retrofit.client.Response response) {
            progressDialog.dismiss();
            if(response == null) {
                dialog = new Dialog(Registration.this, null,"Esa casilla de correo ya está registrada.");
                dialog.show();
                dialog.getButtonAccept().setText("Aceptar");
            } else {
                dialog = new Dialog(Registration.this, "Registro exitoso!", "Te enviamos un mail de confirmación a tu casilla.");
                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Registration.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
                dialog.getButtonAccept().setText("Aceptar");
            }
        }
    }

}
