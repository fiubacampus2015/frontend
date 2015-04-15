package com.fiuba.campus2015.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import static com.fiuba.campus2015.extras.Constants.*;
import static com.fiuba.campus2015.extras.Utils.stringToCalendar;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.CustomAdapter;
import com.fiuba.campus2015.dto.user.Education;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EducationFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener {

    private Spinner spCarreras;
    private Spinner spOrientacion;
    private ListView list;
    private CustomAdapter customAdapter;
    private View myView;
    private DatePicker fechaIngreso;

    public static EducationFragment newInstance(Education education) {
        EducationFragment myFragment = new EducationFragment();

        Bundle args = new Bundle();
        String title = "";
        String branch = "" ;
        String date = "";

        if(!education.careers.isEmpty())
        {
            title = education.careers.get(0).title;
            branch = education.careers.get(0).branch;
            date = education.careers.get(0).initdate;
        }


        args.putString(PROFESION, title);
        args.putString(ORIENTATION, branch);
        args.putString(FECHAINGRESO, date);

        myFragment.setArguments(args);

        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Se inicializan los datos
        myView = inflater.inflate(R.layout.loadeducation_layout, container, false);
        fechaIngreso = (DatePicker) myView.findViewById(R.id.fechaIngreso);
        loadCareer();
        String initdate = getArguments().getString(FECHAINGRESO);
        String profesion = getArguments().getString(PROFESION);


        //Se carga la fecha
        if(!initdate.isEmpty()) {

            Calendar fecha = null;
            try {

                fecha = stringToCalendar(initdate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            fechaIngreso.updateDate(fecha.get(Calendar.YEAR),fecha.get(Calendar.MONTH),fecha.get(Calendar.DAY_OF_MONTH));

        }
        
        //No se muestran los dias en el datepicker
        LinearLayout pickerParentLayout = (LinearLayout) fechaIngreso.getChildAt(0);
        LinearLayout pickerSpinnersHolder = (LinearLayout) pickerParentLayout.getChildAt(0);
        pickerSpinnersHolder.getChildAt(1).setVisibility(View.GONE);

        //Se carga el combo de profesion
        if (!profesion.equals(null) && !profesion.isEmpty()) {
            int spinnerPosition = ((ArrayAdapter<CharSequence>) spCarreras.getAdapter()).getPosition(profesion);
            spCarreras.setSelection(spinnerPosition);
        }

        //Se carga el combo de orientacion
       // String orientacion = getArguments().getString(ORIENTATION);
       // if (!orientacion.equals(null) && !orientacion.isEmpty()) {
        //    int spinnerPosition =  ((ArrayAdapter<CharSequence>) spOrientacion.getAdapter()).getPosition(orientacion);
        //    spOrientacion.setSelection(spinnerPosition);
       // }


        return myView;
    }

    private void loadCareer() {
        spCarreras = (Spinner)myView.findViewById(R.id.spinnerCarreras);
        spOrientacion = (Spinner)myView.findViewById(R.id.spinnerOrientacion);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.carreras, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCarreras.setAdapter(adapter);
        spCarreras.setOnItemSelectedListener(this);
        spOrientacion.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCarreras:
                TypedArray arrayorientaciones = getResources().obtainTypedArray(
                        R.array.array_carrera_a_orientaciones);
                CharSequence[] orientaciones = arrayorientaciones.getTextArray(position);
                arrayorientaciones.recycle();

                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                        getActivity(), android.R.layout.simple_spinner_item,
                        android.R.id.text1, orientaciones);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spOrientacion.setAdapter(adapter);
/*
                TypedArray arrayCourses = getResources().obtainTypedArray(
                        R.array.array_carrera_a_materias);
                CharSequence[] courses = arrayCourses.getTextArray(position);
                arrayCourses.recycle();*/
                //customAdapter = new CustomAdapter(getActivity(), buildRows(courses));
                //list.setAdapter(customAdapter);
                break;
            case R.id.spinnerOrientacion:

                break;
        }

    }

  /*  private List<Row> buildRows(CharSequence[] courses) {
        List<Row> rows = new ArrayList<>();

        for(CharSequence course: courses) {
            rows.add(new Row(course.toString()));
        }

        return rows;
    }*/

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // tururu
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public Bundle getData() {

        Bundle bundle = new Bundle();
        Calendar calendar = Calendar.getInstance();
        calendar.set(fechaIngreso.getYear(), fechaIngreso.getMonth(), fechaIngreso.getDayOfMonth());
        SimpleDateFormat formatter=new SimpleDateFormat(FORMAT_DATETIME);

        bundle.putString(PROFESION, spCarreras.getSelectedItem().toString());
        bundle.putString(ORIENTATION, "");
        bundle.putString(FECHAINGRESO, formatter.format(calendar.getTime()));

        return bundle;
    }
}
