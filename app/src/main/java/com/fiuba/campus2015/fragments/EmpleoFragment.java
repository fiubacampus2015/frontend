package com.fiuba.campus2015.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static com.fiuba.campus2015.extras.Constants.*;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.JobAdapter;
import com.fiuba.campus2015.adapter.RowJob;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;
import java.util.List;

public class EmpleoFragment extends Fragment implements AdapterView.OnItemClickListener {

    private EditText description;
    private TextView dateFrom;
    private ImageView dateFromButton;
    private TextView dateTo;
    private ImageView dateToButton;

    private boolean selected = false;
    private int jobSelected;
    private boolean disable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.jobfragment, container, false);

        description = (EditText) view.findViewById(R.id.trabajeEn);
        dateFrom =  (TextView) view.findViewById(R.id.date_from);
        dateFromButton = (ImageView)view.findViewById(R.id.date_buttonFrom);
        dateTo =  (TextView) view.findViewById(R.id.date_to);
        dateToButton = (ImageView)view.findViewById(R.id.date_buttonTo);

        // Show a datepicker when the dateButton is clicked
        dateFromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener fromDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)  {
                                String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                                dateFrom.setText(date);
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

        dateToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener toDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)  {
                                String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                                dateTo.setText(date);
                            }
                        };

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        toDateSetListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

                dpd.setOnDateSetListener(toDateSetListener);
            }
        });

        disableComponents();

        return view;
    }

    public void disable() {
        disable = true;
    }

    private void disableComponents() {
        if(disable) {
            description.setEnabled(false);
            dateTo.setEnabled(false);
            dateFrom.setEnabled(false);
            dateFromButton.setEnabled(false);
            dateToButton.setEnabled(false);
        }
    }

    /* TODO: cuando agreguemos la lista de trabajos
    private void addJob() {
        RowJob job = new RowJob();
        job.setDescription(description.getText().toString());

        if(validateFields()) {
            jobs.add(job);
            jobAdapter.notifyDataSetChanged();
        }
    }

    private void deleteJob() {
        if (selected) {
            jobAdapter.remove(jobAdapter.getItem(jobSelected));
            jobAdapter.notifyDataSetChanged();
        }
        selected = false;
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        jobSelected = position;
        selected = true;

    }

    private boolean validateFields() {

        //TODO: agregar validaciones

        return true;
    }

    public Bundle getData() {
        Bundle bundle = new Bundle();
        /*int size = jobAdapter.getCount();

        for(int i = 0; i < size; i++) {
            RowJob job = jobAdapter.getItem(i);*/
            bundle.putString(DESCRIPCIONEMPLEO, description.getText().toString());
            bundle.putString(FECHAINGRESOEMPLEO, dateFrom.getText().toString());
            bundle.putString(FECHASALIDAIEMPLEO, dateTo.getText().toString());

        //bundle.putString(CANTIDADEMPLEOS, Integer.toString(size));

        return bundle;
    }
}

