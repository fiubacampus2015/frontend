package com.fiuba.campus2015.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.cards.WelcomeCard;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.fiuba.campus2015.extras.Constants.BIRTHDAY;
import static com.fiuba.campus2015.extras.Constants.DESCRIPCIONEMPLEO;
import static com.fiuba.campus2015.extras.Constants.EMAIL;
import static com.fiuba.campus2015.extras.Constants.FECHAINGRESO;
import static com.fiuba.campus2015.extras.Constants.FECHAINGRESOEMPLEO;
import static com.fiuba.campus2015.extras.Constants.FECHASALIDAIEMPLEO;
import static com.fiuba.campus2015.extras.Constants.GENDER;
import static com.fiuba.campus2015.extras.Constants.LASTNAME;
import static com.fiuba.campus2015.extras.Constants.NAME;
import static com.fiuba.campus2015.extras.Constants.NATIONALITY;
import static com.fiuba.campus2015.extras.Constants.ORIENTATION;
import static com.fiuba.campus2015.extras.Constants.PHONE;
import static com.fiuba.campus2015.extras.Constants.PHOTO;
import static com.fiuba.campus2015.extras.Constants.PROFESION;
import static com.fiuba.campus2015.extras.Utils.stringToCalendar;


public class CompleteProfile extends Fragment {

    private MaterialListView profileInformation;


    public static CompleteProfile newInstance(User user) {
        CompleteProfile fragment = new CompleteProfile();

        Bundle args = new Bundle();
        args.putString(NAME, user.name);
        args.putString(LASTNAME, user.username);
        args.putString(EMAIL, user.email);
        args.putString(PHONE, user.personal.phones.mobile);
        args.putString(NATIONALITY, user.personal.nacionality);
        args.putString(GENDER, user.personal.gender);
        args.putString(BIRTHDAY, user.personal.birthday);
        args.putString(PHOTO, user.personal.photo);

        if(user.job != null && !user.job.companies.isEmpty()) {
            args.putString(DESCRIPCIONEMPLEO, user.job.companies.get(0).place);
            args.putString(FECHAINGRESOEMPLEO, user.job.companies.get(0).initdate);
            args.putString(FECHASALIDAIEMPLEO, user.job.companies.get(0).enddate);
        }

        if(user.education != null && !user.education.careers.isEmpty()) {
            args.putString(PROFESION, user.education.careers.get(0).title);
            args.putString(ORIENTATION, user.education.careers.get(0).branch);
            args.putString(FECHAINGRESO, user.education.careers.get(0).initdate);
        }

        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_complete_profile, container, false);

        profileInformation = (MaterialListView) view.findViewById(R.id.friendProfileInfo);

        load(view);

        return view;
    }

    private void load(View view) {



        BigImageCard personalCard = new BigImageCard(view.getContext());
        personalCard.setDescription(getArguments().getString(NAME) + " " + getArguments().getString(LASTNAME) + "\n" + getArguments().getString(EMAIL) + "\n" + getArguments().getString(PHONE) + "\n" + getArguments().getString(NATIONALITY) + "\n" + Utils.getBirthdayFormatted(getArguments().getString(BIRTHDAY)));
        personalCard.setTag("BIG_IMAGE_CARD");

        if (!getArguments().getString(PHOTO).isEmpty()) {
            Drawable drawable = new BitmapDrawable(getResources(), Utils.getPhoto(getArguments().getString(PHOTO)));
            personalCard.setDrawable(drawable);

        }else
            personalCard.setDrawable(R.drawable.profiledefault);


        //personalCard.setDrawable("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
        personalCard.setDismissible(false);

        profileInformation.add(personalCard);

       String myCareer = getArguments().getString(PROFESION);
        if(myCareer != null) {
            SmallImageCard careerCard = new SmallImageCard(view.getContext());
            careerCard.setDescription(myCareer + "\n" + getArguments().getString(ORIENTATION) + "\n" + Utils.getBirthdayFormatted(getArguments().getString(FECHAINGRESO)));
            careerCard.setTitle("Educación");
            careerCard.setDrawable(R.drawable.ic_school_grey600_18dp);
            careerCard.setDismissible(false);
            careerCard.setTag("SMALL_IMAGE_CARD");

            profileInformation.add(careerCard);
        }

        String job = getArguments().getString(DESCRIPCIONEMPLEO);
        if(job != null) {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateFrom = getDateFormat(getArguments().getString(FECHAINGRESOEMPLEO));
            Date dateTo = getDateFormat(getArguments().getString(FECHASALIDAIEMPLEO));

            SmallImageCard jobCard = new SmallImageCard(view.getContext());
            jobCard.setDrawable(R.drawable.ic_work_grey);
            jobCard.setTitle("Empleo");
            jobCard.setDescription(job + "\n" + formatter.format(dateFrom) + " - " + formatter.format(dateTo));
            jobCard.setTag("BASIC_IMAGE_BUTTONS_CARD");

            profileInformation.add(jobCard);

        }

    }

    private Date getDateFormat(String dateString)
    {
        Calendar calendar;
        try {
            calendar = stringToCalendar(dateString);
            return calendar.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

}
