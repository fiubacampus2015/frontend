package com.fiuba.campus2015.fragments;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.ForumAdapter;
import com.fiuba.campus2015.adapter.MessageAdapter;
import com.fiuba.campus2015.dto.user.Forum;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.extras.RecyclerItemClickListener;
import com.fiuba.campus2015.extras.UrlEndpoints;
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
    private MessageAdapter fileAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar prgrsBar;
    private EditText searchText;
    private PhotoWallDialog photoDialog;
    private VideoDialog videoDialog;
    private MaterialListView mListView;
    private String groupId;

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

        ImageView buttonSearch = (ImageView) myView.findViewById(R.id.search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFilesInGroup();
            }
        });

        ImageView buttonClear= (ImageView) myView.findViewById(R.id.search_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClear(v);
            }
        });

        prgrsBar = (ProgressBar) myView.findViewById(R.id.progressBarCircularIndeterminateFile);
        emptyView = (TextView) myView.findViewById(R.id.empty_view_files);

        //Se agrega esto por que sino no funciona el input con tabs
        searchText = (EditText) myView.findViewById(R.id.search_text);
        searchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchText.requestFocusFromTouch();
                return false;
            }
        });

        mListView = (MaterialListView) myView.findViewById(R.id.listViewFiles);
        this.fileAdapter = new MessageAdapter(myView.getContext(), mListView, getArguments().getString(USERTO),this);

        FloatingActionButton button_actionAddPhoto = (FloatingActionButton) myView.findViewById(R.id.action_addphoto);
        FloatingActionButton button_actionAddVideo = (FloatingActionButton) myView.findViewById(R.id.action_addVideo);
        FloatingActionButton button_actionAddFile = (FloatingActionButton) myView.findViewById(R.id.action_addFile);

        //videoDialog = new VideoDialog(getActivity(), fileAdapter, getArguments().getString(USERTO));
        photoDialog = new PhotoWallDialog(getActivity(), fileAdapter, getArguments().getString(USERTO));

        button_actionAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog.showDialog();
            }
        });


        button_actionAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //videoDialog.showDialog();
            }
        });

        button_actionAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fileDialog.showDialog();
            }
        });

        update();

        return myView;
    }

    public void searchFilesInGroup() {
        SearchGroupFiles task = new SearchGroupFiles();
        try {
            task.execute();
        } catch (Exception x){
            Toast.makeText(getActivity().getApplicationContext(), "Error al buscar archivos.", Toast.LENGTH_SHORT).show();
        }

    }

    public void update() {
        prgrsBar.setVisibility(View.VISIBLE);
        //Suscripcion a los eventos que devuelve el cliente que llama la api
        getFilesInGroup();
    }

    public void searchClear(View view) {
        emptyView.setVisibility(View.INVISIBLE);
        searchText.setText("");
        searchFilesInGroup();
    }

    private  class SearchGroupFiles extends AsyncTask<Void, Void, List<Message>> {

        RestClient restClient;

        public void executeTask() {
            try {
                this.execute();
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            restClient = new RestClient();
            prgrsBar.setEnabled(true);
            prgrsBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected List<Message> doInBackground(Void... params) {
            List<Message> file = null;
            try {

            } catch (Exception ex) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos de archivos.", Toast.LENGTH_SHORT).show();

            }
            return file;
        }

        @Override
        protected void onPostExecute(List<Message> forums) {
            if (forums == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos de archivos.", Toast.LENGTH_SHORT).show();
            } else {
                if(forums.isEmpty())
                    emptyView.setVisibility(View.VISIBLE);
                else
                    emptyView.setVisibility(View.INVISIBLE);

                prgrsBar.setVisibility(View.INVISIBLE);

            }
        }
    }

    //Se llama a este metodo en caso de que no haya error
    @Subscribe
    public void onFileList(ArrayList<Message> msgs) {
        if(msgs.isEmpty())
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.INVISIBLE);

        fileAdapter.setData(msgs);
        prgrsBar.setVisibility(View.GONE);
        //Desuscripcion a los eventos que devuelve el cliente que llama la api
        Application.getEventBus().unregister(this);
    }


    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onResponse(Response responseError) {
        Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los archivos del grupo." + responseError.reason, Toast.LENGTH_SHORT).show();
        prgrsBar.setVisibility(View.GONE);
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
