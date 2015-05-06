package com.fiuba.campus2015.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.gc.materialdesign.views.ButtonFlat;

public class SearchFilter extends AlertDialog.Builder implements AdapterView.OnItemSelectedListener {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private Spinner spCarreras;
    private Spinner spOrientacion;
    private Spinner spNacionality;
    private String carrera;
    private String orientacion;
    private String nacionalidad;

    protected SearchFilter(FragmentActivity activity) {
        super(activity);

        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.filter_search, null);
        setView(dialogView);
        carrera = orientacion = nacionalidad = "";

        setListener();
        loadCareer();
        loadNacionalities();
        setFormat();
        alertDialog = create();
    }

    public void showDialog() {
        alertDialog.show();
    }

    public String getCareer() {
        return carrera;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    private void setFormat() {
        ((TextView) dialogView.findViewById(R.id.textView5)).setTypeface((Typeface) null, 1);
        ((TextView) dialogView.findViewById(R.id.textView8)).setTypeface((Typeface) null, 1);
        ((TextView) dialogView.findViewById(R.id.textView11)).setTypeface((Typeface) null, 1);
    }

    private void setListener() {
        ButtonFlat buttonAccept = (ButtonFlat) dialogView.findViewById(R.id.buttonAceptarSearchContact);
        ButtonFlat buttonCancel = (ButtonFlat) dialogView.findViewById(R.id.buttonCancelarSearchContact);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carrera = spCarreras.getSelectedItem().toString();

                if (!carrera.equals("Linceciatura Sistemas")) {
                    orientacion = spOrientacion.getSelectedItem().toString();
                } else {
                    orientacion = "";
                }
                nacionalidad = spNacionality.getSelectedItem().toString();

                alertDialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void reset() {
        spCarreras.setSelection(0);
        spNacionality.setSelection(0);
        carrera = orientacion = nacionalidad = "";
    }

    private void loadNacionalities() {
        spNacionality = (Spinner) dialogView.findViewById(R.id.spinnerNacionalitiesSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNacionality.setAdapter(adapter);
    }

    private void loadCareer() {
        spCarreras = (Spinner)dialogView.findViewById(R.id.spinnerCarreraSearch);
        spOrientacion = (Spinner)dialogView.findViewById(R.id.spinnerOrientationSearch);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.carreras, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCarreras.setAdapter(adapter);
        spCarreras.setOnItemSelectedListener(this);
        spOrientacion.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCarreraSearch:
                TypedArray arrayorientaciones = context.getResources().obtainTypedArray(
                        R.array.array_carrera_a_orientaciones);
                CharSequence[] orientaciones = arrayorientaciones.getTextArray(position);
                arrayorientaciones.recycle();

                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                        context, android.R.layout.simple_spinner_item,
                        android.R.id.text1, orientaciones);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spOrientacion.setAdapter(adapter);

                break;
            case R.id.spinnerOrientationSearch:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
