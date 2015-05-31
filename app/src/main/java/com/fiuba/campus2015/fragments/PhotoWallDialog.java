package com.fiuba.campus2015.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.ForumMessage;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.MessageAdapter;
import com.fiuba.campus2015.dto.user.Forum;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.squareup.otto.Subscribe;


import org.w3c.dom.Text;

import retrofit.RestAdapter;
import retrofit.client.Response;

import static com.fiuba.campus2015.extras.Utils.checkSizePhoto;
import static com.fiuba.campus2015.extras.Utils.getPhotoString;
import static com.fiuba.campus2015.extras.Utils.getResizedBitmap;

public class PhotoWallDialog extends AlertDialog.Builder {
    private Context context;
    private View dialogView;
    private Activity activity;
    public static int RESULT_LOAD = 1;
    private AlertDialog alertDialog;
    private TextView msgTo;
    private ImageView msgContent;
    private ButtonFloatMaterial buttonImage;
    private SessionManager session;
    private Bitmap photoBitmap;
    private String userTo;
    private WallFragment wallFragment;
    private ProgressBar prgrsBar;

    public PhotoWallDialog(FragmentActivity activity, WallFragment wallFragment, String userTo) {
        super(activity);

        this.userTo = userTo;
        this.wallFragment = wallFragment;
        this.context = activity;
        this.activity = activity;


        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.photo_message_layout, null);
        session = new SessionManager(activity);

        msgTo = (TextView) dialogView.findViewById(R.id.senderName);
        msgTo.setText(session.getUserName() + " " + session.getUserSurname());
        prgrsBar = (ProgressBar) dialogView.findViewById(R.id.progressBarCircularIndeterminatePhoto);

        buttonImage = (ButtonFloatMaterial) dialogView.findViewById(R.id.buttonImage);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });
        msgContent = (ImageView) dialogView.findViewById(R.id.imageMessage);

        setView(dialogView);

        setListener();
        alertDialog = create();
    }


    public void showDialog() {
        alertDialog.show();
    }

    private void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, RESULT_LOAD);

    }
    private void setListener() {
        ImageView buttonAccept = (ImageView) dialogView.findViewById(R.id.sendMsg);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogView.findViewById(R.id.sendMsg).setEnabled(false);
                if (validateData()) {
                    SendMsgTask task = new SendMsgTask();
                    task.execute();
                    reset();
                    alertDialog.dismiss();
                }
            }
        });
    }

    private boolean validateData() {

        boolean isValid = true;
        if (this.msgContent.getDrawable() == null) {
            Toast.makeText(this.getContext(), "No subiste ninguna foto", Toast.LENGTH_SHORT).show();
            isValid=false;
        }

        return isValid;
    }

    public void reset()
    {
        dialogView.findViewById(R.id.sendMsg).setEnabled(true);
        msgContent.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_default));
    }

    private Bitmap getPhoto(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        ContentResolver c = activity.getContentResolver();
        Cursor cursor = c.query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String img_Decodable = cursor.getString(columnIndex);
        cursor.close();

        return  BitmapFactory.decodeFile(img_Decodable);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RESULT_LOAD && resultCode == Activity.RESULT_OK && null != data) {
                Bitmap foto = getPhoto(data);
                if(checkSizePhoto(foto)) {
                    photoBitmap = getResizedBitmap(foto,200,200);
                    msgContent.setImageBitmap(photoBitmap);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this.getContext(), "FOTO", Toast.LENGTH_SHORT).show();
        }

    }

    private class SendMsgTask extends AsyncTask<Void, Void,
            Response> {
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
        protected retrofit.client.Response doInBackground(Void... params) {

            IApiUser api = restAdapter.create(IApiUser.class);
            retrofit.client.Response  response = null;
            try {
                response = api.postMsgToWall(session.getToken(),userTo,new Message(getPhotoString(photoBitmap),Constants.MsgCardType.photo));

            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(retrofit.client.Response response) {
            prgrsBar.setVisibility(View.INVISIBLE);
            wallFragment.update();

        }
    }

}
