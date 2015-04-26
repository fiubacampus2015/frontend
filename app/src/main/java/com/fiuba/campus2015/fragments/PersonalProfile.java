package com.fiuba.campus2015.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Utils;

import static com.fiuba.campus2015.extras.Constants.BIRTHDAY;
import static com.fiuba.campus2015.extras.Constants.EMAIL;
import static com.fiuba.campus2015.extras.Constants.GENDER;
import static com.fiuba.campus2015.extras.Constants.LASTNAME;
import static com.fiuba.campus2015.extras.Constants.NAME;
import static com.fiuba.campus2015.extras.Constants.NATIONALITY;
import static com.fiuba.campus2015.extras.Constants.PHONE;
import static com.fiuba.campus2015.extras.Constants.PHOTO;

public class PersonalProfile extends Fragment {
    private ImageView photo;
    private TextView name;
    private TextView lastName;
    private TextView email;
    private TextView nationality;
    private TextView birthday;
    private TextView gender;
    private TextView phone;
    private View view;

    public static PersonalProfile newInstance(User user) {
        PersonalProfile personalProfile = new PersonalProfile();

        Bundle args = new Bundle();
        args.putString(NAME, user.name);
        args.putString(LASTNAME, user.username);
        args.putString(EMAIL, user.email);
        args.putString(PHONE, user.personal.phones.mobile);
        args.putString(NATIONALITY, user.personal.nacionality);
        args.putString(GENDER, user.personal.gender);
        args.putString(BIRTHDAY, user.personal.birthday);
        args.putString(PHOTO, user.personal.photo);

        personalProfile.setArguments(args);
        return personalProfile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_personal, container, false);

        initialize();
        loadData();
        return view;
    }

    private void loadData() {
        name.setText(getArguments().getString(NAME));
        lastName.setText(getArguments().getString(LASTNAME));
        email.setText(getArguments().getString(EMAIL));
        phone.setText(getArguments().getString(PHONE));
        nationality.setText(getArguments().getString(NATIONALITY));
        setPhoto(getArguments().getString(PHOTO));

        String genero = getArguments().getString(GENDER);
        if(!genero.isEmpty()) {
            gender.setText(Utils.getGender(genero));
        }

        birthday.setText(Utils.getBirthdayFormatted(getArguments().getString(BIRTHDAY)));

    }

    private void initialize() {
        photo = (ImageView) view.findViewById(R.id.imageView);
        name = (TextView) view.findViewById(R.id.nombre_Profile);
        lastName = (TextView) view.findViewById(R.id.apellido_Profile);
        email = (TextView) view.findViewById(R.id.emailProfile);
        nationality = (TextView) view.findViewById(R.id.nacionalidaProfile);
        birthday = (TextView) view.findViewById(R.id.cumpleProfile);
        gender = (TextView) view.findViewById(R.id.generoProfile);
        phone = (TextView) view.findViewById(R.id.celularProfile);
    }

    private void setPhoto(String photoCad) {
        if (!photoCad.isEmpty()) {
            photo.setImageBitmap(Utils.getPhoto(photoCad));
        }

    }
}
