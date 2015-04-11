package com.fiuba.campus2015.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fiuba.campus2015.R;

import java.util.List;


public class JobAdapter extends ArrayAdapter<RowJob> {
    private List<RowJob> jobs;
    private LayoutInflater layoutInflater;

    public JobAdapter(Context context, List<RowJob> objects) {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JobHolder jobHolder;

        if(convertView == null) {
            convertView= layoutInflater.inflate(R.layout.job_row,null);

            jobHolder = new JobHolder();
            jobHolder.description = (TextView)convertView.findViewById(R.id.idDescrip);
            jobHolder.begin = (TextView)convertView.findViewById(R.id.idDesde);
            jobHolder.end = (TextView)convertView.findViewById(R.id.idHasta);

            convertView.setTag(jobHolder);
        } else {
            jobHolder = (JobHolder)convertView.getTag();
        }

        RowJob rowJob = getItem(position);
        jobHolder.description.setText(rowJob.getDescription());
        jobHolder.begin.setText(rowJob.getInicio());
        jobHolder.end.setText(rowJob.getFin());

        return convertView;
    }

    static class JobHolder {
        TextView description;
        TextView begin;
        TextView end;
    }
}










