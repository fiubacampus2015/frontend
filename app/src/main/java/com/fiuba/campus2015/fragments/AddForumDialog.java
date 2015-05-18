package com.fiuba.campus2015.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import retrofit.RestAdapter;
import retrofit.client.Response;

public class AddForumDialog extends AlertDialog.Builder {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private MaterialEditText forumFirstMsg;
    private MaterialEditText forumTitle;
    private SessionManager session;
    private GroupForumsFragment groupforumFragment;

    protected AddForumDialog(FragmentActivity activity, GroupForumsFragment groupforumFragment) {
        super(activity);

        this.groupforumFragment = groupforumFragment;
        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_forum_layout, null);
        session = new SessionManager(context);

        forumTitle = (MaterialEditText) dialogView.findViewById(R.id.forumTitle);
        forumFirstMsg = (MaterialEditText) dialogView.findViewById(R.id.forumFirstMsg);

        setView(dialogView);

        setListener();
        alertDialog = create();
    }

    public void showDialog() {
        alertDialog.show();
    }

    private boolean validateData(){

        if(isEmpty(forumTitle)) {
            ((MaterialEditText) dialogView.findViewById(R.id.forumTitle)).validateWith(new RegexpValidator("Ingresá un título.", "\\d+"));
            return false;
        }
        return true;
    }

    private boolean isEmpty(TextView textview) {
        return (textview.getText().length() == 0);
    }

    private void setListener() {
        ImageView buttonAccept = (ImageView) dialogView.findViewById(R.id.addForum);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (validateData()){
                AddForumTask task = new AddForumTask();
                task.execute();

                alertDialog.dismiss();
                reset();
            }else{

            }
            }
        });

    }

    public void reset() {
        forumTitle.setText("");
        forumFirstMsg.setText("");
    }

    private class AddForumTask extends AsyncTask<Void, Void,
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

                //TODO: POST api forums

            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            groupforumFragment.update();

        }
    }

}