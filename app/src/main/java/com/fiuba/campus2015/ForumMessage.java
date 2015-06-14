package com.fiuba.campus2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dexafree.materialList.controller.IMaterialListAdapter;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.adapter.MessageAdapter;
import com.fiuba.campus2015.dto.user.Forum;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.fragments.PhotoForumDialog;
import com.fiuba.campus2015.fragments.PostForumLinkDialog;
import com.fiuba.campus2015.fragments.PostLinkDialog;
import com.fiuba.campus2015.fragments.WriteForumMsgDialog;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.fiuba.campus2015.extras.Constants.FORUM;
import static com.fiuba.campus2015.extras.Constants.GROUP;
import static com.fiuba.campus2015.extras.Constants.USERTO;


public class ForumMessage  extends ActionBarActivity {
    private Toolbar toolbar;
    private MaterialListView mListView;
    private MessageAdapter msgAdapter;
    private ProgressBar prgrsBar;
    private String forumId;
    private String groupId;
    private WriteForumMsgDialog w_msgDialog;
    private PhotoForumDialog w_photoForumDialog;
    private PostForumLinkDialog linkDialog;
    private TextView emptyView;


    private SessionManager session;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_message_layout);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());
        emptyView = (TextView) findViewById(R.id.empty_viewForum);

        mListView = (MaterialListView) findViewById(R.id.material_listview);


        msgAdapter = new MessageAdapter(this, mListView, session.getUserid(),this);

        FloatingActionButton button_actionAddMeg = (FloatingActionButton) findViewById(R.id.action_write);

        FloatingActionButton button_actionAddPhoto = (FloatingActionButton) findViewById(R.id.action_addphoto);

        FloatingActionButton button_actionAddPlace = (FloatingActionButton) findViewById(R.id.action_addPlace);
        button_actionAddPlace.setVisibility(View.GONE);

        FloatingActionButton button_actionAddVideo = (FloatingActionButton) findViewById(R.id.action_addVideo);
        button_actionAddVideo.setVisibility(View.GONE);

        final FloatingActionButton button_actionAddLink = (FloatingActionButton) findViewById(R.id.action_link);

        prgrsBar = (ProgressBar) findViewById(R.id.progressBarCircularIndeterminate_);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String forumJson = extras.getString(FORUM);
            Forum forum = new Gson().fromJson(forumJson, Forum.class);
            forumId = forum._id;
            groupId = extras.getString(GROUP);
            getSupportActionBar().setTitle(forum.title);
        }

        w_msgDialog = new WriteForumMsgDialog(this,msgAdapter,groupId,forumId);

        w_photoForumDialog = new PhotoForumDialog(this,msgAdapter,groupId,forumId);

        linkDialog = new PostForumLinkDialog(this,msgAdapter,groupId,forumId);

        button_actionAddMeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w_msgDialog.showDialog();
            }
        });

        button_actionAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w_photoForumDialog.showDialog();
            }
        });

        button_actionAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkDialog.showDialog();
            }
        });


        getMessages();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        return super.onOptionsItemSelected(item);
    }

    public void deletedMessage() {
        if(((IMaterialListAdapter)mListView.getAdapter()).isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void getMessages() {
        prgrsBar.setVisibility(View.VISIBLE);
        //Suscripcion a los eventos que devuelve el cliente que llama la api
        callApiMessages();
    }

    public String getForumId() {
        return forumId;
    }
    public String getGroupId() {
        return groupId;
    }



    //Se llama a este metodo en caso de que no haya error
    @Subscribe
    public void onMessageForumList(ArrayList<Message> msgs) {

        if(msgs.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            Application.getEventBus().unregister(this);
            prgrsBar.setVisibility(View.GONE);


        }else if (!msgs.isEmpty() && msgs.get(0) instanceof  Message) {
            emptyView.setVisibility(View.INVISIBLE);
            msgAdapter.setData(msgs);
            msgAdapter.fillArray();
            Application.getEventBus().unregister(this);
            prgrsBar.setVisibility(View.GONE);

        }
    }


    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onResponse(Response responseError) {
        //TODO Mostrar errores en caso de error del request
        prgrsBar.setVisibility(View.GONE);
        Application.getEventBus().unregister(this);

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        w_photoForumDialog.onActivityResult(requestCode, resultCode, data);


    }

    public void callApiMessages()
    {
        Application.getEventBus().register(this);

        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<List<Message>, IApiUser>() {
            @Override
            public List<Message> getResult(IApiUser service) {
                return service.getForumMessages(session.getToken(), groupId, forumId);
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<List<Message>, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }

}
