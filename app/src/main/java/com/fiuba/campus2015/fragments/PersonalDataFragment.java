package com.fiuba.campus2015.fragments;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import static com.fiuba.campus2015.extras.Constants.*;
import static com.fiuba.campus2015.extras.Utils.getResizedBitmap;
import static com.fiuba.campus2015.extras.Utils.stringToCalendar;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.extras.Utils;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PersonalDataFragment extends Fragment {
    private ImageView photoUser;
    private ButtonFloatMaterial buttonImage;
    private View myView;
    public static int RESULT_LOAD = 1;
    private final int KB = 1024;
    private final int MAXSIZE = 8;
    private String img_Decodable_Str;
    private Bitmap photoBitmap;
    private EditText name;
    private EditText username;
    private EditText mail;
    private EditText phone;
    private Spinner nationality;
    private RadioGroup radioGroup;
    private String gender;
    private DatePicker birthday;

    public static PersonalDataFragment newInstance(User user) {
        PersonalDataFragment myFragment = new PersonalDataFragment();

        Bundle args = new Bundle();
        args.putString(NAME, user.name);
        args.putString(LASTNAME, user.username);
        args.putString(EMAIL, user.email);
        args.putString(PHONE, user.personal.phones.mobile);
        args.putString(NATIONALITY, user.personal.nacionality);
        args.putString(GENDER, user.personal.gender);
        args.putString(BIRTHDAY, user.personal.birthday);
        args.putString(PHOTO, user.personal.photo);

        myFragment.setArguments(args);

        return myFragment;
    }



        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.loadpersonaldata_layout, container, false);

        initializeIdComponents();

        name.setText(getArguments().getString(NAME));
        username.setText(getArguments().getString(LASTNAME));
        mail.setText(getArguments().getString(EMAIL));
        phone.setText(getArguments().getString(PHONE));

        //Precarga la nacionalidad
        nationality = (Spinner) myView.findViewById(R.id.idnationality);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(myView.getContext(),
        R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nationality.setAdapter(adapter);

        String nacionalidad = getArguments().getString(NATIONALITY);
        if (!nacionalidad.equals(null) && !nacionalidad.isEmpty()) {
            int spinnerPosition = ((ArrayAdapter<CharSequence>) nationality.getAdapter()).getPosition(nacionalidad);
            nationality.setSelection(spinnerPosition);
        }

        photoUser = (ImageView)myView.findViewById(R.id.idPhoto);
        setPhoto(getArguments().getString(PHOTO));


        buttonImage = (ButtonFloatMaterial) myView.findViewById(R.id.buttonImage);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });

        //Precarga el genero
        gender = getArguments().getString(GENDER);
        if (gender.equals("F")) {
            radioGroup.check(R.id.genderFemaleShow);
        } else {
            radioGroup.check(R.id.genderMaleShow);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 if (checkedId == R.id.genderFemaleShow) {
                     gender = "F";
                     System.out.println(gender);
                 } else if (checkedId == R.id.genderMaleShow) {
                     gender = "M";
                 }
             }
        });



            String birth = getArguments().getString(BIRTHDAY);
            //Se carga la fecha
            if(birth != null) {

                Calendar fecha = null;
                try {

                    fecha = stringToCalendar(birth);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                birthday.updateDate(fecha.get(Calendar.YEAR),fecha.get(Calendar.MONTH),fecha.get(Calendar.DAY_OF_MONTH));

            }



            return myView;
    }

    private void initializeIdComponents() {

        name = (EditText) myView.findViewById(R.id.idname);
        username = (EditText) myView.findViewById(R.id.idsurname);
        phone = (EditText) myView.findViewById(R.id.idphone);
        mail = (EditText) myView.findViewById(R.id.idmail);
        nationality = (Spinner) myView.findViewById(R.id.idnationality);
        radioGroup = (RadioGroup)myView.findViewById(R.id.idradiogroupshow);
        birthday = (DatePicker) myView.findViewById(R.id.birthday);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD && resultCode == Activity.RESULT_OK && null != data) {
                Bitmap foto = getPhoto(data);
                if(extensionValida()) {
                    if(tamañoValido(foto)) {
                        photoBitmap = foto;
                        photoUser.setImageBitmap(getResizedBitmap(photoBitmap,256,256));
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Seleccioná fotos menores a 8MB", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Seleccioná fotos de formato: " + EXTENSIONES[0] + "o " + EXTENSIONES[1]+"",
                            Toast.LENGTH_SHORT).show();
                }
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

        return  BitmapFactory.decodeFile(img_Decodable_Str);
    }

    private boolean extensionValida() {
      // TODO: falta validar
        return true;
    }

    private boolean tamañoValido(Bitmap bitmap) {
        int bytes = bitmap.getRowBytes() * bitmap.getHeight();
        int megaBytes = bytes/(KB*KB);

        if(megaBytes >= MAXSIZE) {
            return false;
        }
        return true;
    }

    private String getPhotoString() {
        if(photoBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        }
        return null;
    }

    private void setPhoto(String photo) {
        if (!photo.isEmpty()) {
            photoUser.setImageBitmap(Utils.getPhoto(photo));
        }
    }

    private void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(galleryIntent, RESULT_LOAD);
    }

    public Bundle getData() {
        Bundle bundle = new Bundle();
        bundle.putString(NAME,name.getText().toString());
        bundle.putString(LASTNAME,username.getText().toString());
        bundle.putString(EMAIL, mail.getText().toString());
        bundle.putString(PHONE, phone.getText().toString());
        bundle.putString(NATIONALITY, nationality.getSelectedItem().toString());
        bundle.putString(GENDER, gender);

        Calendar calendar = Calendar.getInstance();
        calendar.set(birthday.getYear(), birthday.getMonth(), birthday.getDayOfMonth());
        SimpleDateFormat formatter=new SimpleDateFormat(FORMAT_DATETIME);

        bundle.putString(BIRTHDAY, formatter.format(calendar.getTime()));

        String photoString = getPhotoString();
        if(photoString != null) {
            bundle.putString(PHOTO, photoString);
        }else {
            bundle.putString(PHOTO, "");
        }

        return bundle;
    }


}
