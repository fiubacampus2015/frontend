package com.fiuba.campus2015;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.Dialog;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import retrofit.client.Response;

import static com.fiuba.campus2015.extras.Constants.GROUP;
import static com.fiuba.campus2015.extras.Utils.checkSizePhoto;
import static com.fiuba.campus2015.extras.Utils.getPhotoString;
import static com.fiuba.campus2015.extras.Utils.getResizedBitmap;

public class ModifyGroup extends ActionBarActivity {
    private ImageView photoGroup;
    private ButtonFloatMaterial buttonImage;
    public static int RESULT_LOAD = 15;
    private Bitmap photoBitmap;
    private MaterialEditText groupName;
    private MaterialEditText groupDescription;
    private Spinner groupTypeSpinner;
    private Toolbar toolbar;
    private ProgressBar prgrsBar;
    private SessionManager session;
    private String idGroup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdatagroup_layout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        prgrsBar = (ProgressBar) findViewById(R.id.progressBarCircularIndeterminateDataGroup);
        session = new SessionManager(getApplicationContext());

        initialice();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String groupJson = extras.getString(GROUP);
            Group group = new Gson().fromJson(groupJson, Group.class);
            loadData(group);
        }

    }

    private void initialice() {
        buttonImage = (ButtonFloatMaterial) findViewById(R.id.buttonImageGroup_);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(v);
            }
        });

        groupName = (MaterialEditText) findViewById(R.id.idnameGroupEdit);
        groupDescription = (MaterialEditText) findViewById(R.id.idDescGrupoEdit);
        photoGroup = (ImageView) findViewById(R.id.idPhotoGroup_);

        groupTypeSpinner = (Spinner) findViewById(R.id.groupTypeEditGroup);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.group_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupTypeSpinner.setAdapter(adapter);
    }

    public void submitData() {
        prgrsBar.setVisibility(View.VISIBLE);

        if (validateData()) {
            EditGroupTask task = new EditGroupTask(this);
            task.execute();
        }

    }

    private boolean validateData() {
        if (isEmpty(groupName)) {
            ((MaterialEditText) findViewById(R.id.groupName)).validateWith(new RegexpValidator("Ingresá un título.", "\\d+"));
            return false;
        }
        return true;
    }

    private boolean isEmpty(TextView textview) {
        return (textview.getText().length() == 0);
    }

    private void loadData(Group group) {
        idGroup = group._id;
        groupDescription.setText(group.description);
        groupName.setText(group.name);

        if(group.photo != null && !group.photo.isEmpty()) {
            photoBitmap = Utils.getPhoto(group.photo);
            photoGroup.setImageBitmap(photoBitmap);
        }

        String status = getEstadoGrupo(group.status);

        if(status != null && !status.isEmpty()) {
            int spinnerPosition = ((ArrayAdapter<CharSequence>) groupTypeSpinner.getAdapter()).getPosition(status);
            groupTypeSpinner.setSelection(spinnerPosition);
        }

    }

    private void showError() {
        Toast.makeText(this, "Hubo un error al actualizar los datos", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD && resultCode == RESULT_OK && null != data) {
                Bitmap foto = getPhoto(data);
                if (checkSizePhoto(foto)) {
                    photoBitmap = getResizedBitmap(foto, 200, 200);
                    photoGroup.setImageBitmap(photoBitmap);
                }
                else
                    Toast.makeText(this, "Seleccioná fotos menores a 8MB", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }

    private Bitmap getPhoto(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        ContentResolver c = getContentResolver();
        Cursor cursor = c.query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String img_Decodable = cursor.getString(columnIndex);
        cursor.close();

        return BitmapFactory.decodeFile(img_Decodable);
    }

    private void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD);
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(this,null, "Cambios no guardados");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBackPressed(null);
            }
        });
        dialog.addCancelButton("Guardar");
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
        dialog.show();
        dialog.getButtonAccept().setText("Descartar");
    }

    private void myBackPressed(Group groupUpdate) {
        if(groupUpdate != null) {
            Intent intent = new Intent();
            intent.putExtra(GROUP, new Gson().toJson(groupUpdate));
            setResult(RESULT_OK, intent);

        }
        super.onBackPressed();
    }

    public String getStatusGroup(String status)
    {

        if (status.equals("Privado"))
            return "private";
        else if (status.equals("Oculto"))
            return "hidden";
        return "public";
    }

    public String getEstadoGrupo(String estado)
    {
        if(estado != null) {
            if (estado.equals("private"))
                return "Privado";
            else if (estado.equals("hidden"))
                return "Oculto";
            return "Público";
        }
        return "";
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemSubmit = menu.findItem(R.id.action_submit);
        itemSubmit.setVisible(true);
        MenuItem itemSearch = menu.findItem(R.id.action_edit);
        itemSearch.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_submit:
                submitData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class EditGroupTask extends AsyncTask<Void, Void,
            Response> {
        RestClient restClient;
        ModifyGroup dataGroup;
        Group group;

        public EditGroupTask(ModifyGroup dataGroup) {
            this.dataGroup = dataGroup;
        }

        public void executeTask() {
            try {
                this.execute();
            } catch (Exception e) {
            }
        }

        @Override
        protected void onPreExecute() {
            restClient = new RestClient();
        }

        @Override
        protected Response doInBackground(Void... params) {
            Response response = null;
            String myStatus = getStatusGroup(groupTypeSpinner.getSelectedItem().toString());
            group = new Group(new User(session.getUserid()), groupName.getText().toString(),
                    groupDescription.getText().toString(),getPhotoString(photoBitmap),myStatus);
            try {
                response = restClient.getApiService().editGroup(session.getToken(),idGroup, group);

            } catch (Exception x) {
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            prgrsBar.setVisibility(View.GONE);

            if(response != null) {
                // esto para que se actualice groupProfile al volver
                Application.getEventBus().post(group);

                dataGroup.myBackPressed(group);
            }
            else
                dataGroup.showError();

        }
    }
}




