package com.fiuba.campus2015.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

    protected WriteMsgDialog(FragmentActivity activity) {
        super(activity);

        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.write_message_layout, null);

        msgTo = (TextView) dialogView.findViewById(R.id.senderName);
        msgTo.setText("Jimena Tapia"); //TODO: tomar el nombre del user

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
            Response> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
        }

        @Override
        protected retrofit.client.Response doInBackground(Void... params) {

            IApiUser api = restAdapter.create(IApiUser.class);
            retrofit.client.Response  response = null;
            try {
                Calendar calendar = Calendar.getInstance();

                //seria: vos posteas a /api/' + jime_token + '/users/' + peta_user_id +'/wall
                //response = api.post(new Message(msgContent.getText(),null,calendar.getTime(),Constants.MsgCardType.text));

            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(retrofit.client.Response response) {
            /*progressDialog.dismiss();
            if(response == null) {
                dialog = new Dialog(Registration.this, null,"Esa casilla de correo ya está registrada.");
                dialog.show();
                dialog.getButtonAccept().setText("Aceptar");
            } else {
                dialog = new Dialog(Registration.this, "Registro exitoso!", "Te enviamos un mail de confirmación a tu casilla.");
                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Registration.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
                dialog.getButtonAccept().setText("Aceptar");*/

        }
    }

}
