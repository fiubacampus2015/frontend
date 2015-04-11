package com.fiuba.campus2015.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.R;
import java.io.ByteArrayOutputStream;


public class PersonalDataFragment extends Fragment {
    private ImageView photoUser;
    private View myView;
    public static int RESULT_LOAD = 1;
    private String img_Decodable_Str;
    private Bitmap photoBitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.loadpersonaldata_layout, container, false);

        photoUser = (ImageView)myView.findViewById(R.id.idPhoto);
        photoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });
        return myView;
    }

    private void photoShow() {

        // se muestra la foto del usuario, por ahora se muestra la imagen por defecto
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD && resultCode == Activity.RESULT_OK && null != data) {
                photoUser.setImageBitmap(getPhoto(data));
            }
        } catch (Exception e) {
        }

    }

    private Bitmap getPhoto(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        ContentResolver c = getActivity().getContentResolver();
        Cursor cursor = c.query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        img_Decodable_Str = cursor.getString(columnIndex);
        cursor.close();

        photoBitmap = BitmapFactory.decodeFile(img_Decodable_Str);
        return photoBitmap;
    }


    private String getPhotoString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // no quedo otra que hacer esto
        getActivity().startActivityForResult(galleryIntent, RESULT_LOAD);
    }
}
