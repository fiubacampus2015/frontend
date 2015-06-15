package com.fiuba.campus2015.fragments;

import android.annotation.TargetApi;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import static com.fiuba.campus2015.extras.Constants.*;
import static com.fiuba.campus2015.extras.Utils.stringToCalendar;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.CustomAdapter;
import com.fiuba.campus2015.dto.user.Education;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EducationFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener {

    private Spinner spCarreras;
    private Spinner spOrientacion;
    private ListView list;
    private CustomAdapter customAdapter;
    private View myView;
    private TextView fechaIngresoString;
    private java.util.Date fechaIngreso;
    private ImageView fechaIngresoButton;
    private boolean disable;
    private int optionOrientation = -1;
    private CircleProgress circleProgress;
    private MaterialEditText creditosInput;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public static EducationFragment newInstance(Education education) {
        EducationFragment myFragment = new EducationFragment();

        Bundle args = new Bundle();
        String title = "";
        String branch = "" ;
        String date = "";
        String creditos = "";

        if(!education.careers.isEmpty())
        {
            title = education.careers.get(0).title;
            branch = education.careers.get(0).branch;
            date = education.careers.get(0).initdate;
            creditos = education.creditos;
        }


        args.putString(PROFESION, title);
        args.putString(ORIENTATION, branch);
        args.putString(FECHAINGRESO, date);
        args.putString(CREDITOS,creditos);

        myFragment.setArguments(args);

        return myFragment;
    }

    public void disable() {
        disable = true;
    }

    private void disableComponents() {
        if(disable) {
            spCarreras.setEnabled(false);
            spOrientacion.setEnabled(false);
            fechaIngresoButton.setEnabled(false);
            fechaIngresoString.setEnabled(false);
            creditosInput.setVisibility(View.GONE);
        }else{
            circleProgress.setVisibility(View.GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Se inicializan los datos
        myView = inflater.inflate(R.layout.loadeducation_layout, container, false);
        loadCareer();
        String profesion = getArguments().getString(PROFESION);

        creditosInput = (MaterialEditText) myView.findViewById(R.id.idCreditos);
        creditosInput.setText(getArguments().getString(CREDITOS));

        //Se carga la fecha
        fechaIngresoString =  (TextView) myView.findViewById(R.id.date_initString);
        fechaIngresoButton = (ImageView)myView.findViewById(R.id.dateInit_button);

        fechaIngreso = getDateFormat(getArguments().getString(FECHAINGRESO));
        if (fechaIngreso!=null)
            fechaIngresoString.setText(formatter.format(fechaIngreso));

        // Show a datepicker when the dateButton is clicked
        fechaIngresoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener fromDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)  {
                                String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                                fechaIngresoString.setText(date);
                                try {
                                    fechaIngreso = formatter.parse(date);
                                } catch(Exception e) {}
                            }
                        };

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(fromDateSetListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        //Se carga el combo de profesion
        if (!profesion.equals(null) && !profesion.isEmpty()) {
            int spinnerPosition = ((ArrayAdapter<CharSequence>) spCarreras.getAdapter()).getPosition(profesion);
            spCarreras.setSelection(spinnerPosition);
            onItemSelected(spCarreras,null,spinnerPosition,0);
        }

        //Se carga el combo de orientacion
        String orientacion = getArguments().getString(ORIENTATION);
        if (!orientacion.equals(null) && !orientacion.isEmpty() && !profesion.equals("Linceciatura Sistemas")) {
            int spinnerPosition =  ((ArrayAdapter<CharSequence>) spOrientacion.getAdapter()).getPosition(orientacion);
            spOrientacion.setSelection(spinnerPosition);
            optionOrientation = spinnerPosition;
        }

        circleProgress = (CircleProgress) myView.findViewById(R.id.circle_progress);
        circleProgress.setProgress(getAvanceCarrera());

        disableComponents();
        return myView;
    }

    private Integer getAvanceCarrera() {
        Integer avance = 0;
        Double percent = 0.0;

        if (!getArguments().getString(CREDITOS).isEmpty()) avance = Integer.parseInt(getArguments().getString(CREDITOS));
        percent = ((avance.doubleValue() / 240 ) * 100);

        return percent.intValue();
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

    private Date getDateFormat(String dateString)
    {
        Calendar calendar = null;
        try {

            calendar = stringToCalendar(dateString);
            return calendar.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

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
                if(optionOrientation != -1) {
                    spOrientacion.setSelection(optionOrientation);
                    optionOrientation = -1;
                }
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

    private boolean validateFields() {
        Calendar calendar = Calendar.getInstance();

        if(fechaIngreso != null) {
            if(fechaIngreso.compareTo(calendar.getTime()) == 1 ){
                ((MaterialEditText) myView.findViewById(R.id.date_initString)).validateWith(new RegexpValidator("Fecha de Ingreso mayor a hoy?", "\\d+"));
                return false;
            }
        }
        if(creditosInput.getText() != null) {
            //if(Integer.parseInt(creditosInput.getText().toString()) > 240 ){
                ((MaterialEditText) myView.findViewById(R.id.idCreditos)).validateWith(new RegexpValidator("Son más créditos que los totales de la carrera.", "\\d+"));
                return false;
            //}
        }

        return true;
    }


    public Bundle getData() {

        Bundle bundle = new Bundle();

        if (validateFields()) {
            SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DATETIME);
            bundle.putString(FECHAINGRESO, "");
            if (fechaIngreso != null) {
                bundle.putString(FECHAINGRESO, formatter.format(fechaIngreso.getTime()));
            }

            String profesion = spCarreras.getSelectedItem().toString();
            bundle.putString(PROFESION, profesion);

            if (!profesion.equals("Licenciatura Sistemas")) {
                bundle.putString(ORIENTATION, spOrientacion.getSelectedItem().toString());
            } else {
                bundle.putString(ORIENTATION, "");
            }

            bundle.putString(CREDITOS, creditosInput.getText().toString());

        }
        return bundle;
    }
}
