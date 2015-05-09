package com.fiuba.campus2015.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class WriteMsgDialog extends AlertDialog.Builder {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private TextView msgReceiver;
    private MaterialEditText msgContent;

    protected WriteMsgDialog(FragmentActivity activity) {
        super(activity);

        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.write_message_layout, null);

        msgReceiver = (TextView) dialogView.findViewById(R.id.senderName);
        msgReceiver.setText("Jimena Tapia"); //TODO: tomar el nombre del user

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

                msgContent.getText();

                //TODO: devolver contenido al fragment para que se cree una nueva card.

                alertDialog.dismiss();
                reset();
            }
        });

       /* ImageView buttonCancel = (ImageView) dialogView.findViewById(R.id.dismissMsg);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });*/

    }

    public void reset() {
        msgContent.setText("");
    }

}
