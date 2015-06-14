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
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Subscribe;


import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static com.fiuba.campus2015.extras.Utils.checkSizePhoto;
import static com.fiuba.campus2015.extras.Utils.getPhotoString;
import static com.fiuba.campus2015.extras.Utils.getResizedBitmap;

public class PhotoForumDialog extends AlertDialog.Builder {
    private ForumMessage context;
    private View dialogView;
    private Activity activity;
    public static int RESULT_LOAD = 1;
    private AlertDialog alertDialog;
    private TextView msgTo;
    private ImageView msgContent;
    private ButtonFloatMaterial buttonImage;
    private SessionManager session;
    private Bitmap photoBitmap;

    private String forumId;
    private MessageAdapter messageAdapter;
    private String groupId;
    private String pathPhoto;


    public PhotoForumDialog(ForumMessage activity, MessageAdapter messageAdapter, String groupId, String forumId) {
        super(activity);

        this.groupId = groupId;
        this.forumId = forumId;
        this.messageAdapter = messageAdapter;
        this.activity = activity;

        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.photo_message_layout, null);
        session = new SessionManager(activity);

        msgTo = (TextView) dialogView.findViewById(R.id.senderName);
        msgTo.setText(session.getUserName() + " " + session.getUserSurname());


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
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, RESULT_LOAD);

    }
    private void setListener() {
        ImageView buttonAccept = (ImageView) dialogView.findViewById(R.id.sendMsg);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    alertDialog.dismiss();
                    //dialogView.findViewById(R.id.sendMsg).setEnabled(false);
                    postMessage();
                }
            }
        });
    }


    public void reset()
    {
        //dialogView.findViewById(R.id.sendMsg).setEnabled(true);
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
                    pathPhoto = Utils.getRealPathFromURI(activity, data.getData());
                    msgContent.setImageBitmap(photoBitmap);
                }else
                    Toast.makeText(this.getContext(),
                            "Seleccioná fotos menores a 8MB", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }

    }

    private boolean validateData() {

        boolean isValid = true;
        if (photoBitmap == null) {
            Toast.makeText(this.getContext(), "No subiste ninguna foto", Toast.LENGTH_SHORT).show();
            isValid=false;
        }

        return isValid;
    }

    @Subscribe
    public void onResponseClient(retrofit.client.Response response) {
        Application.getEventBus().unregister(this);
        reset();
        this.context.getMessages();

    }

    public void postMessage() {
        Application.getEventBus().register(this);

        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<Response, IApiUser>() {
            @Override
            public Response getResult(IApiUser service) {

                return service.postFileToForum(session.getToken(), groupId, forumId,
                        new TypedFile("multipart/form-data",new java.io.File(pathPhoto)),
                          getPhotoString(photoBitmap), Constants.MsgCardType.photo.toString());
            }
        };

        RestClient restClient = new RestClient();

        RestServiceAsync callApi = new RestServiceAsync<Response, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new com.fiuba.campus2015.services.Response());

    }

}
