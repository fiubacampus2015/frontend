package com.fiuba.campus2015.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.views.ButtonFlat;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import retrofit.client.Response;

import static com.fiuba.campus2015.extras.Utils.getPhotoString;
import static com.fiuba.campus2015.extras.Utils.getResizedBitmap;
import static com.fiuba.campus2015.extras.Utils.checkSizePhoto;


public class AddGroupDialog extends AlertDialog.Builder {
    private View dialogView;
    public static int RESULT_LOAD = 1;
    private AlertDialog alertDialog;
    private MaterialEditText groupDescription;
    private MaterialEditText groupName;
    private FragmentActivity activity;
    private SessionManager session;
    private GroupFragment groupFragment;
    private Spinner groupTypeSpinner;
    private ButtonFloatMaterial buttonImage;
    private Bitmap photoBitmap;
    private final int KB = 1024;
    private final int MAXSIZE = 8;
    private ImageView photoGroup;
    private boolean publicGroup;

    protected AddGroupDialog(FragmentActivity activity, GroupFragment groupFragment) {
        super(activity);

        this.groupFragment = groupFragment;

        this.activity = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_group_layout, null);
        session = new SessionManager(activity.getApplicationContext());

        groupName = (MaterialEditText) dialogView.findViewById(R.id.groupName);
        groupDescription = (MaterialEditText) dialogView.findViewById(R.id.groupDescription);

        buttonImage = (ButtonFloatMaterial) dialogView.findViewById(R.id.buttonImage);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });
        photoGroup = (ImageView) dialogView.findViewById(R.id.idPhoto);

        //Carga del combo desplegable para la nacionalidad
        groupTypeSpinner = (Spinner)dialogView.findViewById(R.id.groupType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dialogView.getContext(),
                R.array.group_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupTypeSpinner.setAdapter(adapter);

        setView(dialogView);

        setListener();
        alertDialog = create();
    }

    private void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, RESULT_LOAD);

    }

    public void showDialog() {
        alertDialog.show();
    }

    private boolean validateData(){

        if(isEmpty(groupName)) {
            ((MaterialEditText) dialogView.findViewById(R.id.groupName)).validateWith(new RegexpValidator("Ingresá un título.", "\\d+"));
            return false;
        }

        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RESULT_LOAD && resultCode == Activity.RESULT_OK && null != data) {
                Bitmap foto = getPhoto(data);
                    if(checkSizePhoto(foto)) {
                        photoBitmap = getResizedBitmap(foto,200,200);
                        photoGroup.setImageBitmap(photoBitmap);
                    }
            }
        } catch (Exception e) {
        }

    }

    private boolean isEmpty(TextView textview) {
        return (textview.getText().length() == 0);
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

    private void setListener() {
        ButtonFlat buttonAccept = (ButtonFlat) dialogView.findViewById(R.id.addGroup);
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

        ButtonFlat buttonCancel = (ButtonFlat) dialogView.findViewById(R.id.cancelNewGroup);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    alertDialog.dismiss();
                    reset();
            }
        });

    }

    public void reset() {
        groupName.setText("");
        groupDescription.setText("");
    }

    private class AddGroupTask extends AsyncTask<Void, Void,
            Response> {
        RestClient restClient;



        public void executeTask() {
            try {
                this.execute();
            } catch (Exception e) {
            }
        }


        @Override
        protected void onPreExecute() {
            restClient = new RestClient();

        }

        @Override
        protected Response doInBackground(Void... params) {

            Response  response = null;
            try {

                response = restClient.getApiService().createGroup(session.getToken(),
                        new Group(new User(session.getUserid()),groupName.getText().toString(),
                                groupDescription.getText().toString(),getPhotoString(photoBitmap)));

            } catch (Exception x) {}

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {

            if(response != null) {
                groupFragment.update();
            }

        }
    }

}
