package com.fiuba.campus2015.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.FileAdapter;
import com.fiuba.campus2015.dto.user.Forum;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.otto.Subscribe;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;

import static com.fiuba.campus2015.extras.Constants.GROUP;
import static com.fiuba.campus2015.extras.Constants.GROUPOWNER;
import static com.fiuba.campus2015.extras.Constants.USERTO;

public class GroupFilesFragment extends Fragment {
    private View myView;
    private SessionManager session;
    private TextView emptyView;
    private ProgressBar progressBar;
    private EditText searchText;
    private PhotoWallDialog photoDialog;
    private VideoDialog videoDialog;
    private String groupId;
    private FileDialog fileDialog;
    private FileAdapter fileAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;

    public static GroupFilesFragment newInstance(Group group) {
        GroupFilesFragment fragment = new GroupFilesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        args.putString(GROUP, group._id);

        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }

    private void openFile(String path) {

    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.group_files_fragment, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        groupId = getArguments().getString(GROUP);

        ImageView buttonSearch = (ImageView) myView.findViewById(R.id.search_files);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFilesInGroup();
            }
        });

        ImageView buttonClear = (ImageView) myView.findViewById(R.id.search_files_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClear(v);
            }
        });

        emptyView = (TextView) myView.findViewById(R.id.empty_view_files);

        //Se agrega esto por que sino no funciona el input con tabs
        searchText = (EditText) myView.findViewById(R.id.search_files_text);
        searchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchText.requestFocusFromTouch();
                return false;
            }
        });


        progressBar = (ProgressBar) myView.findViewById(R.id.progressBarCircularIndeterminateFile);

        fileAdapter = new FileAdapter(getActivity(), this);
        recyclerView = (RecyclerView) myView.findViewById(R.id.listViewFiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(fileAdapter);

        fileDialog = new FileDialog(getActivity(), fileAdapter,groupId);
        videoDialog = new VideoDialog(getActivity(), fileAdapter,groupId);
        photoDialog = new PhotoWallDialog(getActivity(), fileAdapter,groupId);

        FloatingActionButton button_actionAddPhoto = (FloatingActionButton) myView.findViewById(R.id.action_addphoto);
        FloatingActionButton button_actionAddVideo = (FloatingActionButton) myView.findViewById(R.id.action_addVideo);
        FloatingActionButton button_actionAddFile = (FloatingActionButton) myView.findViewById(R.id.action_addFile);

        button_actionAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog.showDialog();
            }
        });

        button_actionAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDialog.showDialog();
            }
        });

        button_actionAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileDialog.showDialog();
            }
        });

        update();

        return myView;
    }

    public void searchFilesInGroup() {


    }


    @Override
    public void onStart() {
        super.onStart();


    }

    public void update() {
        progressBar.setVisibility(View.VISIBLE);
        //Suscripcion a los eventos que devuelve el cliente que llama la api
        getFilesInGroup();
    }

    public void searchClear(View view) {
        emptyView.setVisibility(View.INVISIBLE);
        searchText.setText("");
        searchFilesInGroup();
    }

    public void downloadFile(String fileId, String name, String path) {

        DownloadFileFromURL downloadFile = new DownloadFileFromURL();

        downloadFile.execute(path, name);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoDialog.onActivityResult(requestCode, resultCode, data);
        fileDialog.onActivityResult(requestCode, resultCode, data);
        videoDialog.onActivityResult(requestCode, resultCode, data);
    }

    //Se llama a este metodo en caso de que no haya error
    @Subscribe
    public void onFileList(ArrayList<com.fiuba.campus2015.dto.user.File> files) {

        if (!files.isEmpty() && files.get(0) instanceof com.fiuba.campus2015.dto.user.File)
        {
            emptyView.setVisibility(View.INVISIBLE);
            fileAdapter.setFiles(files);
            Application.getEventBus().unregister(this);


        }else if (files.isEmpty())
        {
            emptyView.setVisibility(View.VISIBLE);
            Application.getEventBus().unregister(this);

        }
        progressBar.setVisibility(View.GONE);
        //Desuscripcion a los eventos que devuelve el cliente que llama la api
    }


    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onResponse(Response responseError) {
        Application.getEventBus().unregister(this);
        Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los archivos del grupo." + responseError.reason, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }


    public void getFilesInGroup() {
        Application.getEventBus().register(this);

        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<List<com.fiuba.campus2015.dto.user.File>, IApiUser>() {
            @Override
            public List<com.fiuba.campus2015.dto.user.File> getResult(IApiUser service) {
                return service.getGroupFiles(session.getToken(), groupId, searchText.getText().toString());
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<List<com.fiuba.campus2015.dto.user.File>, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }

    private void showDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Descargando archivo. Por favor espere...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

              showDialog();
        }


        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(UrlEndpoints.URL_API + f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                //
                File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String fileName = f_url[1];
                File file = new File(downloadFolder,fileName); //por ahora se ignora el nombre real

                // Output stream to write file
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {

            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
                  pDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

        }

    }

}