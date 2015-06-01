package com.fiuba.campus2015.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuba.campus2015.MainActivity;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.dto.user.Personal;
import com.fiuba.campus2015.dto.user.Phone;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.Dialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import retrofit.RestAdapter;
import retrofit.client.Response;

public class WriteMsgDialog extends AlertDialog.Builder {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private TextView msgTo;
    private MaterialEditText msgContent;
    private SessionManager session;
    private String userTo;
    private WallFragment wallFragment;
    private ProgressBar prgrsBar;

    protected WriteMsgDialog(FragmentActivity activity, WallFragment wallFragment, String userTo) {
        super(activity);

        this.userTo = userTo;
        this.wallFragment = wallFragment;
        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.write_message_layout, null);
        session = new SessionManager(context);
        prgrsBar = (ProgressBar) dialogView.findViewById(R.id.progressBarCircularIndeterminateMessage);

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

                SendMsgTask task = new SendMsgTask();
                task.execute();

                alertDialog.dismiss();
                reset();
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
            prgrsBar.setEnabled(true);
            prgrsBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Message doInBackground(Void... params) {

            IApiUser api = restAdapter.create(IApiUser.class);
            Message response = null;
            try {
                response = api.postMsgToWall(session.getToken(),userTo,new Message(msgContent.getText().toString(),Constants.MsgCardType.text));

            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(Message response) {
            prgrsBar.setVisibility(View.INVISIBLE);
            wallFragment.update();

        }
    }

}
