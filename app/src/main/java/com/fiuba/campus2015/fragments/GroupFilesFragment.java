package com.fiuba.campus2015.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.otto.Subscribe;

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

    public static GroupFilesFragment newInstance(Group group) {
        GroupFilesFragment fragment = new GroupFilesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        args.putString(GROUP, group._id);

        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
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

        ImageView buttonClear= (ImageView) myView.findViewById(R.id.search_files_clear);
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

        fileDialog = new FileDialog(getActivity(), fileAdapter);
        videoDialog = new VideoDialog(getActivity(), fileAdapter);
        photoDialog = new PhotoWallDialog(getActivity(), fileAdapter);

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

//        update();

        return myView;
    }

    public void searchFilesInGroup() {


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

    public void downloadFile(String fileId, String name) {
        Toast.makeText(getActivity(),"sin implementar, descargando " + name, Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoDialog.onActivityResult(requestCode,resultCode,data);
        fileDialog.onActivityResult(requestCode,resultCode,data);
        videoDialog.onActivityResult(requestCode,resultCode,data);
    }

    //Se llama a este metodo en caso de que no haya error
    @Subscribe
    public void onFileList(ArrayList<Message> msgs) {
        if(msgs.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.INVISIBLE);

     //   fileAdapter.setData(msgs);
        progressBar.setVisibility(View.GONE);
        //Desuscripcion a los eventos que devuelve el cliente que llama la api
        Application.getEventBus().unregister(this);
    }


    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onResponse(Response responseError) {
        Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los archivos del grupo." + responseError.reason, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        Application.getEventBus().unregister(this);
    }


    public void getFilesInGroup()
    {
        Application.getEventBus().register(this);
        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<List<Message>, IApiUser>() {
            @Override
            public List<Message> getResult(IApiUser service) {
                return service.getGroupFiles(session.getToken(), groupId, searchText.getText().toString());
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<List<Message>, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }

}
