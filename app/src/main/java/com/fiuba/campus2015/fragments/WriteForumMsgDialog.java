package com.fiuba.campus2015.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.ForumMessage;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.MessageAdapter;
import com.fiuba.campus2015.dto.user.Forum;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;


import retrofit.RestAdapter;
import retrofit.client.Response;

public class WriteForumMsgDialog extends AlertDialog.Builder {
    private ForumMessage context;
    private View dialogView;
    private AlertDialog alertDialog;
    private TextView msgTo;
    private MaterialEditText msgContent;
    private SessionManager session;
    private String forumId;
    private MessageAdapter messageAdapter;
    private String groupId;

    private WallFragment wallFragment;


    public WriteForumMsgDialog(ForumMessage activity,MessageAdapter messageAdapter, String groupId,String forumId) {
        super(activity);

        this.groupId = groupId;
        this.forumId = forumId;
        this.messageAdapter = messageAdapter;
        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.write_message_layout, null);
        session = new SessionManager(activity);

        msgTo = (TextView) dialogView.findViewById(R.id.senderName);
        msgTo.setText(session.getUserName() + " " + session.getUserSurname());

        msgContent = (MaterialEditText) dialogView.findViewById(R.id.msgContent);

        setView(dialogView);

        setListener();
        alertDialog = create();
    }

    public void showDialog() {
        alertDialog.show();
    }

    private void setListener() {
        ImageView buttonAccept = (ImageView) dialogView.findViewById(R.id.sendMsg);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                postMessage();
                //alertDialog.dismiss();
            }
        });

        ImageView buttonClear = (ImageView) dialogView.findViewById(R.id.dismissMsg);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

    }

    @Subscribe
    public void onResponseClient(retrofit.client.Response response) {
        Application.getEventBus().unregister(this);
        reset();
        this.context.getMessages();
    }


    public void reset() {
        msgContent.setText("");
    }

    public void postMessage() {
        Application.getEventBus().register(this);

        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<Response, IApiUser>() {
            @Override
            public Response getResult(IApiUser service) {
                return service.postMsgToForum(session.getToken(), groupId, forumId, new Message(msgContent.getText().toString(), Constants.MsgCardType.text));
            }
        };

        RestClient restClient = new RestClient();

        RestServiceAsync callApi = new RestServiceAsync<Response, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new com.fiuba.campus2015.services.Response());

    }

}
