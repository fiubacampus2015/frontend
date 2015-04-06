package com.fiuba.campus2015;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import com.fiuba.campus2015.adapter.CustomAdapter;
import com.fiuba.campus2015.adapter.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ismael on 05/04/15.
 */
public class LoadEducation extends ActionBarActivity implements AdapterView.OnItemSelectedListener,
        OnItemClickListener {

    private Spinner spCarreras;
    private Spinner spOrientacion;
    private ListView list;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadeducation_layout);
        loadCareer();
        list = (ListView) findViewById(R.id.list);
    }

    private void loadCareer() {
        spCarreras = (Spinner)findViewById(R.id.spinnerCarreras);
        spOrientacion = (Spinner)findViewById(R.id.spinnerOrientacion);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.carreras, android.R.layout.simple_spinner_item);
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
                        this, android.R.layout.simple_spinner_item,
                        android.R.id.text1, orientaciones);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spOrientacion.setAdapter(adapter);

                TypedArray arrayCourses = getResources().obtainTypedArray(
                        R.array.array_carrera_a_materias);
                CharSequence[] courses = arrayCourses.getTextArray(position);
                arrayCourses.recycle();
                customAdapter = new CustomAdapter(this, buildRows(courses));
                list.setAdapter(customAdapter);
                break;
            case R.id.spinnerOrientacion:/*
               */
                break;
        }

    }

    private List<Row> buildRows(CharSequence[] courses) {
        List<Row> rows = new ArrayList<>();

        for(CharSequence course: courses) {
            rows.add(new Row(course.toString()));
        }

        return rows;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // tururu
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
