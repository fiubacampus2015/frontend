package com.fiuba.campus2015.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.JobAdapter;
import com.fiuba.campus2015.adapter.PageAdapter;
import com.fiuba.campus2015.adapter.RowJob;

import java.util.ArrayList;
import java.util.List;

public class EmpleoFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView list;
    private Button addButton;
    private Button deleteButton;
    private EditText begin;
    private EditText end;
    private EditText description;
    private List<RowJob> jobs;
    private JobAdapter jobAdapter;
    private boolean selected = false;
    private int jobSelected;
    private static int YEARDIGITS = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.jobfragment, container, false);

        jobs = new ArrayList<>();
        list = (ListView) view.findViewById(R.id.listJob);
        list.setOnItemClickListener(this);
        jobAdapter = new JobAdapter(getActivity(), jobs);
        list.setAdapter(jobAdapter);

        addButton = (Button) view.findViewById(R.id.buttonx);
        deleteButton = (Button) view.findViewById(R.id.idDelete);
        description = (EditText) view.findViewById(R.id.editText3);
        begin = (EditText) view.findViewById(R.id.editText4);
        end = (EditText) view.findViewById(R.id.editText5);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJob();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteJob();
            }
        });

        return view;
    }

    private void addJob() {
        RowJob job = new RowJob();
        job.setDescription(description.getText().toString());
        job.setInicio(begin.getText().toString());
        job.setFin(end.getText().toString());

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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        jobSelected = position;
        selected = true;

    }

    private boolean validateFields() {
        String desde = begin.getText().toString();
        String hasta = end.getText().toString();
        String descrip = description.getText().toString();
        boolean result = false;

        if(descrip.length() > 0 && desde.length() == YEARDIGITS  && hasta.length() == YEARDIGITS) {
            try {
                int date1 = Integer.parseInt(desde);
                int date2 = Integer.parseInt(hasta);

                if(date2 > date1) {
                    result = true;
                }
            } catch (NumberFormatException e){}
        }
        return result;
    }

}

