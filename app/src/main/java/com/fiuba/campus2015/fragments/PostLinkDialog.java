package com.fiuba.campus2015.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.MessageAdapter;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import retrofit.RestAdapter;
import retrofit.client.Response;

public class PostLinkDialog extends AlertDialog.Builder {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private TextView msgTo;
    private MaterialEditText msgContent;
    private SessionManager session;
    private String userTo;
    private MessageAdapter msgList;

    protected PostLinkDialog(FragmentActivity activity, MessageAdapter msgList, String userTo) {
        super(activity);

        this.userTo = userTo;
        this.context = activity;
        this.msgList = msgList;

        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.post_link_layout, null);
        session = new SessionManager(context);

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

        ImageView buttonClear = (ImageView) dialogView.findViewById(R.id.dismissMsg);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        ImageView buttonLink = (ImageView) dialogView.findViewById(R.id.addLink);
        buttonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Patterns.WEB_URL.matcher(msgContent.getText()).matches()){
                    alertDialog.dismiss();
                    SendMsgTask task = new SendMsgTask();
                    task.execute();
                } else {
                    ((MaterialEditText) dialogView.findViewById(R.id.msgContent)).validateWith(new RegexpValidator("Esto no es un link!", "\\d+"));

                }
            }
        });

    }

    public void reset() {
        msgContent.setText("");
    }

    private class SendMsgTask extends AsyncTask<Void, Void,
            Message> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
        }

        @Override
        protected Message doInBackground(Void... params) {

            IApiUser api = restAdapter.create(IApiUser.class);
            Message  response = null;
            try {
                response = api.postMsgToWall(session.getToken(),userTo,new Message(msgContent.getText().toString(),Constants.MsgCardType.link));

            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(Message response) {
            reset();
            msgList.addMsg(response);

        }
    }

}
