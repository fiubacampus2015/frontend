package com.fiuba.campus2015;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import com.fiuba.campus2015.fragments.MyBoard;
import com.fiuba.campus2015.navigationdrawer.NavigationDrawerCallbacks;
import com.fiuba.campus2015.navigationdrawer.NavigationDrawerFragment;
import com.fiuba.campus2015.session.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by gonzalovelasco on 29/3/15.
 */
public class Board extends ActionBarActivity  implements NavigationDrawerCallbacks {
    private SweetAlertDialog pDialog;
    private Toolbar toolbar;
    private NavigationDrawerFragment drawerFragment;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());

        TextView userMail = (TextView)findViewById(R.id.txtUserEmail);
        TextView userName = (TextView)findViewById(R.id.txtCompleteName);


        userMail.setText(session.getUserMail());
        userName.setText(session.getUserName()+" "+session.getUserSurname());
        String photo = session.getUserPhoto();
        if (!photo.isEmpty())
        {
            ImageView imageAvatar = (ImageView)findViewById(R.id.imgAvatar);
            byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
            imageAvatar.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
        }



        drawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.fragment_drawer);
        drawerFragment.setup(R.id.fragment_drawer,(DrawerLayout)findViewById(R.id.drawer_layout), toolbar);





    }

    /*
       -Aqui se crean los fragments que se lanzan del menu desplegable
       -Para crear los items que aparecen en el menu ir --> NavigationDrawerFragment: getMenu()
   */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Intent intent = null;
        switch (position) {
            case 0: /* MURO */
                fragmentManager.beginTransaction().replace(R.id.container,new MyBoard()).commit();
                break;
            case 1: /* PERFIL */
                intent = new Intent(Board.this,Profile.class);
                startActivity(intent);
                break;

            case 2: /* CONFIGURACION */
                intent = new Intent(Board.this,Configuration.class);
                startActivity(intent);
                break;
            case 3:  /* SALIR */
                shutdownMessage();
                session.logoutUser();
                break;
            default: break;
        }
    }

    private void shutdownMessage() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Cerrando sesión")
                .setContentText("Espere un momento por favor.");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerFragment.isDrawerOpen())
            drawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
