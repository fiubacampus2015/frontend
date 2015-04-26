package com.fiuba.campus2015;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import static com.fiuba.campus2015.extras.Constants.USER;
import com.fiuba.campus2015.adapter.ProfileAdapter;
import com.fiuba.campus2015.asyntask.LoadUserDataTask;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.fragments.IProfile;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class Profile extends ActionBarActivity implements IProfile{
    private ViewPager vpPager;
    private ProfileAdapter adapter;
    private Toolbar toolbar;
    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setOffscreenPageLimit(3);

        SessionManager session = new SessionManager(getApplicationContext());
        LoadUserDataTask loadTask = new LoadUserDataTask(this, session.getToken(), session.getUserid());
        loadTask.executeTask();
    }

    public void setUser(User user) {
        if(user == null) {
            Toast.makeText(this, "Hubo un error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        this.user = user;
        adapter= new ProfileAdapter(getSupportFragmentManager(), user);
        vpPager.setAdapter(adapter);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSubmit = menu.findItem(R.id.action_submit);
        itemSubmit.setVisible(false);
        MenuItem itemSearch = menu.findItem(R.id.action_edit);
        itemSearch.setVisible(true);
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
            case R.id.action_edit:
                Intent intent = new Intent(this, ProfileEditable.class);
                intent.putExtra(USER, new Gson().toJson(user));
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }




}
