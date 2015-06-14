package com.fiuba.campus2015.fragments;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.FileAdapter;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.session.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static com.fiuba.campus2015.extras.Constants.SEP;

import retrofit.RestAdapter;
import retrofit.mime.TypedFile;

public class FileDialog extends AlertDialog.Builder {
    private FileAdapter fileAdapter;
    private ProgressBar prgrsBar;
    private Context context;
    private View dialogView;
    private Activity activity;
    public static int RESULT_LOAD = 158;
    private ButtonFloatMaterial buttonFile;
    private ImageView buttonSend;
    private ImageView buttonDelete;
    private SessionManager session;
    private AlertDialog alertDialog;
    private Uri uriFile;
    private boolean sending;
    private String realPath;
    private final int KB = 1024;
    private final int MAXSIZE = 8;
    private String fileBase64;
    private TextView nameFileTextView;
    private TextView sizeFileTextView;
    private TextView name_TextView;
    private TextView size_TextView;
    private String nameFile;
    private String groupTo;

    public FileDialog(final FragmentActivity activity, FileAdapter fileAdapter, String groupTo) {
        super(activity);

        this.context = activity;
        this.activity = activity;
        this.fileAdapter = fileAdapter;
        this.groupTo = groupTo;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.file_dialog_layout, null);
        session = new SessionManager(activity);

        initialice();
        hiddenText();
        sending = false;

        setListener();

        setView(dialogView);
        alertDialog = create();
    }

    private void initialice() {
        prgrsBar = (ProgressBar) dialogView.findViewById(R.id.progressBarCircularIndeterminateFile_);
        buttonSend = (ImageView) dialogView.findViewById(R.id.sendFile);
        buttonFile= (ButtonFloatMaterial) dialogView.findViewById(R.id.buttonAddFile);
        nameFileTextView = (TextView) dialogView.findViewById(R.id.nameFile_);
        sizeFileTextView = (TextView) dialogView.findViewById(R.id.idSizeFile);
        name_TextView = (TextView) dialogView.findViewById(R.id.nombreFile_);
        size_TextView = (TextView) dialogView.findViewById(R.id.id_sizefile);
        buttonDelete = (ImageView) dialogView.findViewById(R.id.delete_File);

    }

    private void hiddenText() {
        nameFileTextView.setVisibility(View.INVISIBLE);
        sizeFileTextView.setVisibility(View.INVISIBLE);
        name_TextView.setVisibility(View.INVISIBLE);
        size_TextView.setVisibility(View.INVISIBLE);
    }

    private void showText() {
        nameFileTextView.setVisibility(View.VISIBLE);
        sizeFileTextView.setVisibility(View.VISIBLE);
        name_TextView.setVisibility(View.VISIBLE);
        size_TextView.setVisibility(View.VISIBLE);
    }

    private void clear() {
        nameFileTextView.setText("");
        sizeFileTextView.setText("");
    }

    private void setListener() {
        buttonFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sending) {
                    Toast.makeText(activity, "Esperá a que termine de enviar el archivo.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if(realPath != null) {
                        byte[] data = fileToBytes(realPath);

                        if (data != null) {
                            if (validateSize(data)) {
                                fileBase64 = Base64.encodeToString(data, Base64.NO_WRAP);
                                SendFileTask task = new SendFileTask();
                                task.execute();
                            }
                        }
                    }
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sending) {
                    reset();
                } else {
                    Toast.makeText(activity, "Esperá a que termine de enviar el archivo.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getSize() {
        byte[] data = fileToBytes(realPath);

        int size = data.length;
        String unit = " B";

        if(size >= KB) {
            size = size/KB;
            unit = " KB";

            if(size >= KB) {
                size = size/KB;
                unit = " MB";
            }
        }
        return "  " + Integer.toString(size) + unit;
    }

    private boolean validateSize(byte[] data) {
        int size = data.length;
        int megaBytes = size/(KB*KB);

        if(megaBytes >= MAXSIZE) {
            Toast.makeText(activity,"Seleccioná archivos menores a 8MB.",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void reset() {
        uriFile = null;
        realPath = null;
        sending = false;
        clear();
        hiddenText();
    }

    private byte[] fileToBytes(String path) {
        File tempFile = new File(path);

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);
        } catch (Exception e) {}

        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }



    private void getFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        activity.startActivityForResult(intent, RESULT_LOAD);
    }

    public void showDialog() {
        alertDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD && resultCode == Activity.RESULT_OK && null != data) {
            this.uriFile = data.getData();

            File file = new File(uriFile.getPath());
            realPath = file.getAbsolutePath();

            // esto porque cuando son archivos multimedia solo devuelve la Uri
            if(!realPath.contains(".")) {
                realPath = Utils.getRealPathFromURI(activity, uriFile);
            }

            if(realPath != null) {
                int pos = realPath.lastIndexOf(SEP) + 1;
                nameFile = realPath.substring(pos);

                nameFileTextView.setText(nameFile);
                sizeFileTextView.setText(getSize());
                showText();
            }
        }
    }


    private class SendFileTask extends AsyncTask<String, Void, Message> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();

            prgrsBar.setEnabled(true);
            prgrsBar.setVisibility(View.VISIBLE);
            sending = true;
        }


        @Override
        protected Message doInBackground(String... params) {
            IApiUser api = restAdapter.create(IApiUser.class);
            Message message = null;

            try {
            message = api.postFiles(session.getToken(),groupTo,
                    new TypedFile("multipart/form-data",new java.io.File(realPath)),
                    "",Constants.MsgCardType.file.toString());

            } catch (Exception x) { }
            return message;
        }

        @Override
        protected void onPostExecute(Message message) {
            prgrsBar.setVisibility(View.INVISIBLE);

            if (message!=null) {

                com.fiuba.campus2015.dto.user.File file =
                        new com.fiuba.campus2015.dto.user.File();
                file.originalName = message.originalName;
                file.path = message.path;
                file._id = message._id;
                file.typeOf = message.typeOf;
                file.user = message.user._id;

                fileAdapter.addFile(file);
            }
            reset();
            alertDialog.dismiss();
        }
    }
}
