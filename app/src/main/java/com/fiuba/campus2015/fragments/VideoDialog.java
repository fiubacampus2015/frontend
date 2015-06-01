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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.MessageAdapter;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import retrofit.RestAdapter;


public class VideoDialog extends AlertDialog.Builder {
    private Activity activity;
    private View dialogView;
    private AlertDialog alertDialog;
    private TextView msgTo;
    private ImageView imagePreview;
    private SessionManager session;
    private String userTo;
    private WallFragment wallFragment;
    private final static int REQUEST_VIDEO = 89;
    private Uri uriVideo;
    private ProgressBar prgrsBar;
    private MessageAdapter msgAdapter;
    private final int KB = 1024;
    private final int MAXSIZE = 8;
    private boolean sending;
    private String realPath;

    protected VideoDialog(FragmentActivity activity, WallFragment wallFragment, MessageAdapter msgAdapter, String userTo) {
        super(activity);

        this.msgAdapter = msgAdapter;
        this.userTo = userTo;
        this.wallFragment = wallFragment;
        this.activity = activity;
        sending = false;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.select_video_layout, null);
        session = new SessionManager(activity);

        prgrsBar = (ProgressBar) dialogView.findViewById(R.id.progressBarCircularIndeterminateVideo);
        imagePreview = (ImageView) dialogView.findViewById(R.id.videopreview);
        msgTo = (TextView) dialogView.findViewById(R.id.senderName);
        msgTo.setText(session.getUserName() + " " + session.getUserSurname());

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
                if(sending) {
                    Toast.makeText(wallFragment.getActivity(), "Espera que termine de enviar el video",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if(realPath != null) {
                        byte[] data = videoToBytes(realPath);

                        if (data != null) {
                            if (validateSize(data)) {
                                String videoEncoded = encodeVideoToString(data);
                                SendVideoTask task = new SendVideoTask();
                                task.execute(videoEncoded);
                            }
                        } else {
                            Toast.makeText(wallFragment.getActivity(),"Error al convertir el video en bytes",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(wallFragment.getActivity(), "Aún no seleccionaste un video",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ImageView buttonClear = (ImageView) dialogView.findViewById(R.id.dismissMsg);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sending)
                    reset();
                else
                    Toast.makeText(wallFragment.getActivity(), "Espera que termine de enviar el video",
                            Toast.LENGTH_SHORT).show();
            }
        });

        ImageView videoCapture = (ImageView)dialogView.findViewById(R.id.capturecamera);
        videoCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sending) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                    activity.startActivityForResult(intent, REQUEST_VIDEO);
                } else
                    Toast.makeText(wallFragment.getActivity(), "Espera que termine de enviar el video",
                            Toast.LENGTH_SHORT).show();

            }
        });

        ImageView videoGallery = (ImageView)dialogView.findViewById(R.id.videogallery);
        videoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sending) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    activity.startActivityForResult(intent, REQUEST_VIDEO);
                } else
                    Toast.makeText(wallFragment.getActivity(), "Espera que termine de enviar el video",
                            Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getRealPathFromURI(Activity activity, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(activity, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO && resultCode == Activity.RESULT_OK && null != data) {
            this.uriVideo = data.getData();
            realPath =  getRealPathFromURI(wallFragment.getActivity(), uriVideo);

            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(realPath, MediaStore.Video.Thumbnails.MINI_KIND);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(wallFragment.getResources(),thumb);
            imagePreview.setImageDrawable(bitmapDrawable);
        }
    }

    private byte[] videoToBytes(String path) {
        File tempFile = new File(path);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);
        } catch (Exception e) {}

        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    private String encodeVideoToString(byte[] videoBytes) {
        return  Base64.encodeToString(videoBytes, Base64.DEFAULT);
    }

    private boolean validateSize(byte[] data) {
        int size = data.length;
        int megaBytes = size/(KB*KB);

        if(megaBytes >= MAXSIZE) {
            Toast.makeText(wallFragment.getActivity(),"Seleccioná videos menores a 8MB",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void reset() {
        uriVideo = null;
        imagePreview.setImageDrawable(null);
        realPath = null;
        sending = false;
    }

    private class SendVideoTask extends AsyncTask<String, Void, Message> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();

            prgrsBar.setEnabled(true);
            prgrsBar.setVisibility(View.VISIBLE);
            sending = true;
        }

        @Override
        protected Message doInBackground(String... params) {
            IApiUser api = restAdapter.create(IApiUser.class);
            Message  message = null;

            try {
               message =  api.postMsgToWall(session.getToken(), userTo, new Message(params[0],
                        Constants.MsgCardType.video));

            } catch (Exception x) { }

            // probando envio directo al muro
//            return new Message(params[0], Constants.MsgCardType.video);


            return message;
        }

        @Override
        protected void onPostExecute(Message message) {
            prgrsBar.setVisibility(View.INVISIBLE);

           if(message != null)
              msgAdapter.addMsg(message);


            // probando envio directo al muro
/*
           User user = new User(session.getUserName(), session.getUserSurname(),"","",null);
            message.user = user;
            msgAdapter.addMsg(message);
*/
            reset();
            alertDialog.dismiss();
        }
    }

}
