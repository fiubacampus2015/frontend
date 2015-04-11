package com.fiuba.campus2015.fragments;

import android.content.res.TypedArray;
import android.graphics.Color;
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

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.CustomAdapter;

public class EducationFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener {

    private Spinner spCarreras;
    private Spinner spOrientacion;
    private ListView list;
    private CustomAdapter customAdapter;
    private View myView;
    private DatePicker fechaIngreso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.loadeducation_layout, container, false);

        loadCareer();
        //list = (ListView) myView.findViewById(R.id.list);

        //No se muestran los dias en el datepicker
        fechaIngreso = (DatePicker) myView.findViewById(R.id.fechaIngreso);
        LinearLayout pickerParentLayout = (LinearLayout) fechaIngreso.getChildAt(0);
        LinearLayout pickerSpinnersHolder = (LinearLayout) pickerParentLayout.getChildAt(0);
        pickerSpinnersHolder.getChildAt(1).setVisibility(View.GONE);

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

                TypedArray arrayCourses = getResources().obtainTypedArray(
                        R.array.array_carrera_a_materias);
                CharSequence[] courses = arrayCourses.getTextArray(position);
                arrayCourses.recycle();
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
}
