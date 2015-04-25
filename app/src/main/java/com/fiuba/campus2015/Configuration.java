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
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.widgets.Dialog;

public class Configuration extends ActionBarActivity {

    private Toolbar toolbar;
    private Switch deactivatedAccount;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        deactivatedAccount = (Switch) findViewById(R.id.switchDesactivarCuenta);
        deactivatedAccount.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean b) {
                if (b) {

                    Dialog dialog = new Dialog(Configuration.this,"Desactivar Cuenta", "¿ Realmente querés desactivar tu cuenta?");
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
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSubmit = menu.findItem(R.id.action_submit);
        itemSubmit.setVisible(true);
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
        switch (item.getItemId()) {
            case R.id.action_submit:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
