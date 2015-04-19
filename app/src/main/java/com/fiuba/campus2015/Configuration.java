package com.fiuba.campus2015;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gc.materialdesign.views.Switch;

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
                    System.out.println("SIII");
                    //TODO: abrir ventanita para confirmar el desactivado de la cuenta por parte del usuario
                    //TODO: cerrar sesion y postear el borrado de la cuenta
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
