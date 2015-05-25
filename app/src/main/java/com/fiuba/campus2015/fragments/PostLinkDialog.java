package com.fiuba.campus2015.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;

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
    private WallFragment wallFragment;


    protected PostLinkDialog(FragmentActivity activity, WallFragment wallFragment, String userTo) {
        super(activity);

        this.userTo = userTo;
        this.wallFragment = wallFragment;
        this.context = activity;
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
                SendMsgTask task = new SendMsgTask();
                task.execute();

                alertDialog.dismiss();
                reset();
            }
        });

    }

    public void reset() {
        msgContent.setText("");
    }

    private class SendMsgTask extends AsyncTask<Void, Void,
            Response> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
        }

        @Override
        protected Response doInBackground(Void... params) {

            IApiUser api = restAdapter.create(IApiUser.class);
            Response  response = null;
            try {
                response = api.postMsgToWall(session.getToken(),userTo,new Message(msgContent.getText().toString(),Constants.MsgCardType.link));

            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            wallFragment.update();

        }
    }

}
