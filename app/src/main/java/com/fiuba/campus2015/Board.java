package com.fiuba.campus2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.fiuba.campus2015.fragments.LoadFragment;
import com.fiuba.campus2015.fragments.MyBoard;
import com.fiuba.campus2015.navigationdrawer.NavigationDrawerCallbacks;
import com.fiuba.campus2015.navigationdrawer.NavigationDrawerFragment;

/**
 * Created by gonzalovelasco on 29/3/15.
 */
public class Board extends ActionBarActivity  implements NavigationDrawerCallbacks {

    private Toolbar toolbar;
    private NavigationDrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

        switch (position) {
            case 0:
                fragment = new MyBoard();
                break;
            case 1:
                fragment = new LoadFragment();
                break;
            default: break;
        }
        fragmentManager.beginTransaction().replace(R.id.container,fragment).commit();
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
