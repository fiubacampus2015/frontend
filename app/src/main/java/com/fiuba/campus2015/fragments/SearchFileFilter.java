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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuba.campus2015.R;
import com.gc.materialdesign.views.ButtonFlat;

public class SearchFileFilter extends AlertDialog.Builder implements AdapterView.OnItemSelectedListener {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private Spinner spExtensiones;
    private String extension;
    private String title;
    private EditText titleText;
    private GroupFilesFragment groupFileFragment;

    protected SearchFileFilter(FragmentActivity activity, GroupFilesFragment parentFragment) {
        super(activity);

        this.context = activity;
        this.groupFileFragment = parentFragment;

        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.filter_file_search, null);
        setView(dialogView);
        titleText = (EditText) dialogView.findViewById(R.id.editText_title);
        title = extension = "";

        setListener();
        loadExtension();
        setFormat();
        alertDialog = create();
    }

    private void loadExtension() {
        spExtensiones = (Spinner) dialogView.findViewById(R.id.spinnerExtensionsSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.extensiones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExtensiones.setAdapter(adapter);
    }

    // si se coloco algun filtro de busqueda: true
    public boolean filter() {
        return (!title.isEmpty() || !extension.isEmpty());
    }

    public void showDialog() {
        alertDialog.show();
    }

    public String getName() {
        return title;
    }

    private void setFormat() {
        ((TextView) dialogView.findViewById(R.id.textView5)).setTypeface((Typeface) null, 1);
        ((TextView) dialogView.findViewById(R.id.textView14)).setTypeface((Typeface) null, 1);
    }

    private void setListener() {
        ButtonFlat buttonAccept = (ButtonFlat) dialogView.findViewById(R.id.buttonAceptarSearchContact);
        ButtonFlat buttonCancel = (ButtonFlat) dialogView.findViewById(R.id.buttonCancelarSearchContact);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extension = spExtensiones.getSelectedItem().toString();
                title = titleText.getText().toString();

                groupFileFragment.searchFilesInGroup(false);

                alertDialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupFileFragment.searchClear(v);
                alertDialog.dismiss();
            }
        });
    }

    public void reset() {
        spExtensiones.setSelection(0);
        title = extension = "";
        titleText.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerExtensionsSearch:
                TypedArray arrayentensiones = context.getResources().obtainTypedArray(
                        R.array.extensiones);
                CharSequence[] extensiones = arrayentensiones.getTextArray(position);
                arrayentensiones.recycle();

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
