package com.fiuba.campus2015.adapter;

/**
 * Created by ismael on 05/04/15.
 */
public class Row {
    private String course;
    private boolean checked;

    public Row(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
