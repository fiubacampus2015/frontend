package com.fiuba.campus2015.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static com.fiuba.campus2015.extras.Constants.*;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.JobAdapter;
import com.fiuba.campus2015.adapter.RowJob;

import com.fiuba.campus2015.dto.user.Job;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmpleoFragment extends Fragment implements AdapterView.OnItemClickListener {

    private EditText description;
    private TextView dateFromString;
    private ImageView dateFromButton;
    private TextView dateToString;
    private Date dateTo;
    private Date dateFrom;
    private ImageView dateToButton;

    private boolean selected = false;
    private int jobSelected;
    private boolean disable;
    View view;

    public static EmpleoFragment newInstance(Job empleo) {
        EmpleoFragment myFragment = new EmpleoFragment();

        Bundle args = new Bundle();
        String place = "";
        String datefrom = "" ;
        String dateTo = "";

        if(!empleo.companies.isEmpty())
        {
            place = empleo.companies.get(0).place;
            datefrom = empleo.companies.get(0).initdate;
            dateTo = empleo.companies.get(0).enddate;
        }

        args.putString(DESCRIPCIONEMPLEO, place);
        args.putString(FECHAINGRESOEMPLEO, datefrom);
        args.putString(FECHASALIDAIEMPLEO, dateTo);


        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.jobfragment, container, false);

        description = (EditText) view.findViewById(R.id.trabajeEn);
        dateFromString =  (TextView) view.findViewById(R.id.date_from);
        dateFromButton = (ImageView)view.findViewById(R.id.date_buttonFrom);
        dateToString =  (TextView) view.findViewById(R.id.date_to);
        dateToButton = (ImageView)view.findViewById(R.id.date_buttonTo);

        // Show a datepicker when the dateButton is clicked
        dateFromButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener fromDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)  {
                                String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                                dateFromString.setText(date);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    dateFrom = formatter.parse(date);
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

        dateToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener toDateSetListener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)  {
                                String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                                dateToString.setText(date);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    dateTo = formatter.parse(date);
                                } catch(Exception e) {}
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
            dateToString.setEnabled(false);
            dateFromString.setEnabled(false);
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

        if (dateFrom != null && dateTo != null) {
            if(dateFrom.compareTo(dateTo) == 1){
                ((MaterialEditText) view.findViewById(R.id.date_from)).validateWith(new RegexpValidator("Desde mayor al Hasta? Mmm...", "\\d+"));
                ((MaterialEditText) view.findViewById(R.id.date_to)).validateWith(new RegexpValidator("", "\\d+"));
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    public Bundle getData() {
        Bundle bundle = new Bundle();
        SimpleDateFormat formatter=new SimpleDateFormat(FORMAT_DATETIME);
        /*int size = jobAdapter.getCount();

        for(int i = 0; i < size; i++) {
            RowJob job = jobAdapter.getItem(i);*/
           bundle.putString(DESCRIPCIONEMPLEO, description.getText().toString());
           bundle.putString(FECHAINGRESOEMPLEO, formatter.format(dateFrom.getTime()));
           bundle.putString(FECHASALIDAIEMPLEO, formatter.format(dateTo.getTime()));

        //bundle.putString(CANTIDADEMPLEOS, Integer.toString(size));



        return bundle;
    }
}

