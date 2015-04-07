package com.fiuba.campus2015.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fiuba.campus2015.R;

import java.util.List;

/**
 * Created by ismael on 05/04/15.
 */
public class CustomAdapter extends ArrayAdapter<Row> {
    private LayoutInflater layoutInflater;

    public CustomAdapter(Context context, List<Row> rows) {
        super(context, 0, rows);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if(convertView == null) {
            holder = new Holder();

            convertView= layoutInflater.inflate(R.layout.listview_row_courses, null);
            holder.setTextViewCourse((TextView)convertView.findViewById(R.id.textViewRow));
            holder.setCheckBox((CheckBox) convertView.findViewById(R.id.checkBox));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final Row row = getItem(position);
        holder.getTextViewCourse().setText(row.getCourse());

        holder.getCheckBox().setTag(row.getCourse());
        holder.getCheckBox().setChecked(row.isChecked());
        changeBackground(getContext(), convertView, row.isChecked());
        final View fila = convertView;
        holder.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(row.getCourse().equals(buttonView.getTag().toString())) {
                    row.setChecked(isChecked);
                    changeBackground(CustomAdapter.this.getContext(), fila, isChecked);
                }
            }
        });

        return convertView;
    }


    class Holder {
        TextView course;
        CheckBox checkBox;

        public void setTextViewCourse(TextView course) { this.course = course; }
        public  TextView getTextViewCourse() { return course; }
        public void setCheckBox(CheckBox checkBox) { this.checkBox = checkBox; }
        public CheckBox getCheckBox() { return checkBox;}
    }

    /**
     * Set the background of a row based on the value of its checkbox value. Checkbox has its own style.
     */
    @SuppressWarnings("deprecation")
    private void changeBackground(Context context, View row, boolean checked) {
        if (checked) {
            row.setBackgroundDrawable((context.getResources().getDrawable(R.drawable.listview_selector_checked)));
        } else {
            row.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.listview_selector));
        }
    }
}
