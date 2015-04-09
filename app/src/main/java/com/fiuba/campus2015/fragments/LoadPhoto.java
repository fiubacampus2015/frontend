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
import android.widget.Button;
import android.widget.ImageView;

import com.fiuba.campus2015.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by ismael on 09/04/15.
 */
public class LoadPhoto extends Fragment {
    public static int RESULT_LOAD = 1;
    private String img_Decodable_Str;
    private ProgressDialog progressDialog;
    private Bitmap photoBitmap;
    private View myView;
    private ImageView photoUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView =inflater.inflate(R.layout.loadphoto_layout, container, false);

        photoUser = (ImageView)myView.findViewById(R.id.imageView2);
        photoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });

        Button subir = (Button)myView.findViewById(R.id.buttonSubirFoto);
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoBitmap != null) {
                    // falta comprobar que no supere los 8MB antes de enviar

                    //LoadPhotoTask task = new LoadPhotoTask();
                    //task.execute(getPhotoString());
                } else {

                }
            }
        });

        Button ver = (Button)myView.findViewById(R.id.buttonVerFoto);
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se va al perfil a ver la foto cargada

            }
        });

        return myView;
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
        //ojo que es getActivity porque esta dentro de un frafment
        getActivity().startActivityForResult(galleryIntent, RESULT_LOAD);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD && resultCode == Activity.RESULT_OK && null != data) {
                photoUser.setImageBitmap(getPhoto(data));

            } else {
                //    Toast.makeText(this, "Elija nuevamente la foto", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            //  Toast.makeText(this, "Excepcion", Toast.LENGTH_SHORT).show();
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
}
