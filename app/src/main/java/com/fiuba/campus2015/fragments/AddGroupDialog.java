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
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit.RestAdapter;
import retrofit.client.Response;

public class AddGroupDialog extends AlertDialog.Builder {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private MaterialEditText groupDescription;
    private MaterialEditText groupName;
    private SessionManager session;
    private GroupFragment groupFragment;
    private boolean publicGroup;


    protected AddGroupDialog(FragmentActivity activity, GroupFragment groupFragment) {
        super(activity);

        this.groupFragment = groupFragment;
        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_group_layout, null);
        session = new SessionManager(context);

        groupName = (MaterialEditText) dialogView.findViewById(R.id.groupName);
        groupDescription = (MaterialEditText) dialogView.findViewById(R.id.groupDescription);



        setView(dialogView);

        setListener();
        alertDialog = create();
    }

    public void onPublicGroupRadioButtonClicked() {
        // Is the button now checked?
        boolean checked = ((RadioButton) dialogView).isChecked();

        // Check which radio button was clicked
        switch(dialogView.getId()) {
            case R.id.publicGroup:
                if (checked)
                    publicGroup= true;
                break;
            case R.id.privateGroup:
                if (checked)
                    publicGroup= false;
                break;
        }
    }

    public void showDialog() {
        alertDialog.show();
    }

    private boolean validateData(){
        //TODO: validar campos obligatorios para la creacion de un grupo
        return true;
    }

    private void setListener() {
        ImageView buttonAccept = (ImageView) dialogView.findViewById(R.id.addGroup);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (validateData()){
                AddGroupTask task = new AddGroupTask();
                task.execute();

                alertDialog.dismiss();
                reset();
            }else{

            }
            }
        });

    }

    public void reset() {
        groupName.setText("");
        groupDescription.setText("");
    }

    private class AddGroupTask extends AsyncTask<Void, Void,
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

                //TODO: POST api group

            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            groupFragment.update();

        }
    }

}
