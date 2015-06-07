package com.fiuba.campus2015;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.services.LocationSender;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.widgets.Dialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

public class Configuration extends ActionBarActivity {

    private Toolbar toolbar;
    private Button deactivatedAccount;
    private Switch ubicacionActual;
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

        ubicacionActual = (Switch) findViewById(R.id.switchUbicacionContacto);
        ubicacionActual.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean b) {

                if (b)
                {
                    //TODO: servicio de ubicacion actual
                    LocationSender locationSender =new LocationSender();
                    locationSender.send(getApplicationContext(),session.getToken(),session.getUserid());
                    ubicacionActual.setChecked(true);
                } else
                {
                    ubicacionActual.setChecked(false);
                }
            }
        });

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


}
