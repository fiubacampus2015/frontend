package com.fiuba.campus2015.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.Utils;
import com.google.gson.Gson;

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
import static com.fiuba.campus2015.extras.Constants.USER;
import static com.fiuba.campus2015.extras.Utils.stringToCalendar;


public class CompleteProfile extends Fragment {
    private TextView name;
    private TextView phone;
    private TextView nacionality;
    private TextView mail;
    private TextView nacimiento;
    private TextView career;
    private TextView orientation;
    private TextView empleo;
    private TextView dateFromString;
    private TextView dateToString;
    private TextView fechaIngreso;
    private TextView labelEmpleo;
    private TextView labelEducacion;


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

        initializeViews(view);
        load();


        return view;
    }

    private void load() {
        name.setText(getArguments().getString(NAME) + " " + getArguments().getString(LASTNAME));
        mail.setText(getArguments().getString(EMAIL));
        phone.setText(getArguments().getString(PHONE));
        nacionality.setText(getArguments().getString(NATIONALITY));
        nacimiento.setText(Utils.getBirthdayFormatted(getArguments().getString(BIRTHDAY)));

        String myCareer = getArguments().getString(PROFESION);
        if(myCareer != null) {
            career.setText(myCareer);
            orientation.setText(getArguments().getString(ORIENTATION));
            String fechaIngreso_ = Utils.getBirthdayFormatted(getArguments().getString(FECHAINGRESO));
            if (fechaIngreso_ != null) {
                fechaIngreso.setText(fechaIngreso_);
            }
        }

        String job = getArguments().getString(DESCRIPCIONEMPLEO);
        if(job != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            empleo.setText(getArguments().getString(DESCRIPCIONEMPLEO));
            Date dateFrom = getDateFormat(getArguments().getString(FECHAINGRESOEMPLEO));
            Date dateTo = getDateFormat(getArguments().getString(FECHASALIDAIEMPLEO));
            if (dateFrom!=null)
                dateFromString.setText(formatter.format(dateFrom));
            if (dateTo!=null)
                dateToString.setText(formatter.format(dateTo));
        }

        labelEmpleo.setTypeface((Typeface) null, 1);
        labelEducacion.setTypeface((Typeface) null, 1);
    }


    private void initializeViews(View view) {
        name = (TextView) view.findViewById(R.id.nombresPerfilCOmpleto);
        phone = (TextView) view.findViewById(R.id.telefonoPerfilCompleto);
        nacionality = (TextView) view.findViewById(R.id.nacionalidadPerfilCOmpleto);
        mail = (TextView) view.findViewById(R.id.mailPerfilcompleto);
        nacimiento = (TextView) view.findViewById(R.id.nacimientoPerfilCompleto);

        labelEducacion = (TextView) view.findViewById(R.id.titleEducacionPerfil);
        career = (TextView) view.findViewById(R.id.carreraPerfilCOmpleto);
        orientation = (TextView) view.findViewById(R.id.orientacionPerfilCompleto);
        fechaIngreso = (TextView) view.findViewById(R.id.fechaIngresoPerfilCompleto);

        labelEmpleo = (TextView) view.findViewById(R.id.mailPerfilcompleto);
        empleo = (TextView) view.findViewById(R.id.empleoPerfilCOmpleto);
        dateFromString = (TextView) view.findViewById(R.id.fechaInicioPerfilCOmpleto);
        dateToString = (TextView) view.findViewById(R.id.fechaFinPerfilCOmpleto);

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
