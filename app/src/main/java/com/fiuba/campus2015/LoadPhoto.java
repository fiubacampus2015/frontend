package com.fiuba.campus2015;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fiuba.campus2015.services.Response;

import java.io.ByteArrayOutputStream;

import retrofit.RestAdapter;

/**
 * Created by ismael on 04/04/15.
 */
public class LoadPhoto extends ActionBarActivity {
    private static int RESULT_LOAD = 1;
    private  String img_Decodable_Str;
    private ProgressDialog progressDialog;
    private Bitmap photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadphoto_layout);

        Button  edit = (Button)findViewById(R.id.buttonEditarFoto);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });

        Button subir = (Button)findViewById(R.id.buttonSubirFoto);
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photo != null) {
                    // falta comprobar que no supere los 8MB antes de enviar

                    LoadPhotoTask task = new LoadPhotoTask();
                    task.execute(getPhotoString());
                } else {
                    Toast.makeText(LoadPhoto.this, "Aún no elige la foto que desea subir",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button ver = (Button)findViewById(R.id.buttonVerFoto);
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // se va al perfil a ver la foto cargada

            }
        });
    }

    private String getPhotoString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                img_Decodable_Str = cursor.getString(columnIndex);
                cursor.close();

                ImageView imgView = (ImageView) findViewById(R.id.imageView2);
                photo = BitmapFactory.decodeFile(img_Decodable_Str);
                imgView.setImageBitmap(photo);

            } else {
                Toast.makeText(this, "Elija nuevamente la foto", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Excepcion", Toast.LENGTH_SHORT).show();
        }
    }

    private class LoadPhotoTask extends AsyncTask<String, Void, Response> {
        RestAdapter restAdapter;


        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://fiubacampus-staging.herokuapp.com")
                    .build();
            progressDialog = ProgressDialog.show(LoadPhoto.this, "Por favor espere", "Subiendo...");
        }

        @Override
        protected Response doInBackground(String... params) {
            // se requiere el id y token para cargar los datos

            // params[0]
            try { // simulando
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Response response) {
            progressDialog.dismiss();
            //
        }
    }
}
