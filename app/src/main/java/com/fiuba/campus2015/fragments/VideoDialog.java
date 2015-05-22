package com.fiuba.campus2015.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit.RestAdapter;
import retrofit.client.Response;

public class VideoDialog extends AlertDialog.Builder {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private TextView msgTo;
    private ImageView imagePreview;
    private MaterialEditText msgContent;
    private SessionManager session;
    private String userTo;
    private WallFragment wallFragment;
    private final static int REQUEST_VIDEO = 1;
    private Uri uriVideo;
    private ProgressBar prgrsBar;


    protected VideoDialog(FragmentActivity activity, WallFragment wallFragment, String userTo) {
        super(activity);

        this.userTo = userTo;
        this.wallFragment = wallFragment;
        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.select_video_layout, null);
        session = new SessionManager(context);

        prgrsBar = (ProgressBar) dialogView.findViewById(R.id.progressBarCircularIndeterminateVideo);
        imagePreview = (ImageView) dialogView.findViewById(R.id.videopreview);
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
                if(imagePreview.getDrawable() != null) {
                    SendVideoTask task = new SendVideoTask();
                    task.execute();

                }

            }
        });

        ImageView buttonClear = (ImageView) dialogView.findViewById(R.id.dismissMsg);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   reset();

            }
        });

        ImageView videoCapture = (ImageView)dialogView.findViewById(R.id.capturecamera);
        videoCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                wallFragment.onRelativePathVideo(REQUEST_VIDEO, intent);
            }
        });

        ImageView videoGallery = (ImageView)dialogView.findViewById(R.id.videogallery);
        videoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*");
                wallFragment.onRelativePathVideo(REQUEST_VIDEO, intent);
            }
        });

    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(wallFragment.getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void setUri(Uri uriVideo) {
        this.uriVideo = uriVideo;
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(getRealPathFromURI(uriVideo),
                MediaStore.Video.Thumbnails.MINI_KIND);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(wallFragment.getResources(),thumb);
        imagePreview.setImageDrawable(bitmapDrawable);
    }
    public void reset() {
        uriVideo = null;
        imagePreview.setImageDrawable(null);
        msgContent.setText("");
    }

    private class SendVideoTask extends AsyncTask<Void, Void,
            Response> {
      //  RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            //restAdapter = new RestAdapter.Builder().setEndpoint(UrlEndpoints.URL_API).build();
            prgrsBar.setEnabled(true);
            prgrsBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected retrofit.client.Response doInBackground(Void... params) {
         //   IApiUser api = restAdapter.create(IApiUser.class);
           // retrofit.client.Response  response = null;
            try {
                //response = api.postMsgToWall(session.getToken(),userTo,new Message(msgContent.getText().toString(), Constants.MsgCardType.text));

                // simulando la carga de video
                Thread.sleep(1000);
            } catch (Exception x) {}

            return null;
        }

        @Override
        protected void onPostExecute(retrofit.client.Response response) {
            prgrsBar.setVisibility(View.INVISIBLE);

            // probando creacion de mensaje
            User user = new User(session.getUserName(), session.getUserSurname(),"","",null);
            Message msg = new Message(msgContent.getText().toString(),Constants.MsgCardType.video);
            msg.user = user;
            wallFragment.addVideoCard(msg, imagePreview.getDrawable(), uriVideo);
            reset();
            alertDialog.dismiss();
        }
    }

}
