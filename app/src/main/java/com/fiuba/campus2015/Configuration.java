package com.fiuba.campus2015;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.services.LocationSender;
import com.fiuba.campus2015.services.LocationService;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.widgets.Dialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import static android.widget.CompoundButton.*;

public class Configuration extends ActionBarActivity implements
        CompoundButton.OnCheckedChangeListener{

    private Toolbar toolbar;
    private Button deactivatedAccount;
    private SwitchCompat ubicacionActual;
    private SessionManager session;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());

        deactivatedAccount = (Button) findViewById(R.id.desactivarCuenta);
        deactivatedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(Configuration.this, "Desactivar Cuenta", "¿ Realmente querés desactivar tu cuenta?");
                dialog.addCancelButton("No");

                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Aceptar");
                        //TODO: abrir ventanita para mostrar que la app está procesando
                        //TODO: cerrar sesion y postear el borrado de la cuenta

                    }
                });
                dialog.show();

                ButtonFlat acceptButton = (ButtonFlat) dialog.findViewById(R.id.button_accept);
                acceptButton.setText("Si");

            }
        });

        ubicacionActual = (SwitchCompat) findViewById(R.id.switchUbicacionContacto);
        if(session.isLocationEnabled())
        {
            boolean mBool = true;
            ubicacionActual.setChecked(mBool);
        }else
        {
            boolean mBool =false;
            ubicacionActual.setChecked(mBool);
        }

        ubicacionActual.setOnCheckedChangeListener(this);

      /*  ubicacionActual.setOnCheckedChangeListener({
            @Override
            public void onCheck(boolean b) {

                if (b) {
                    session.enableLocation();
                    //ubicacionActual.setChecked(true);
                   // startServiceLocation();

                } else {
                    session.disableLocation();
                    //ubicacionActual.setChecked(false);
                    //stopServiceLocation();
                }
            }
        });*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchUbicacionContacto:
                if (isChecked)
                {
                    session.enableLocation();
                    startServiceLocation();
                }
                else {
                    session.disableLocation();
                    stopServiceLocation();
                }
                break;
            default:
                break;
        }

    }
    private void startServiceLocation()
    {
        Intent intent = new Intent(this, LocationService.class);
        startService(intent);
    }
    private void stopServiceLocation()
    {
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);

        LocationSender locationSender =new LocationSender();
        locationSender.stop(session.getToken(),session.getUserid());
    }
}
