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
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.extras.RecyclerItemClickListener;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;

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

    public static GroupFilesFragment newInstance(String groupId) {
        GroupFilesFragment fragment = new GroupFilesFragment();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.group_files_fragment, container, false);
        session = new SessionManager(getActivity().getApplicationContext());

        ImageView buttonSearch = (ImageView) myView.findViewById(R.id.search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchGroups();
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


        recyclerView = (RecyclerView) myView.findViewById(R.id.listViewFiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //Se agrega esto por que sino no funciona el input con tabs
        searchText = (EditText) myView.findViewById(R.id.search_text);
        searchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchText.requestFocusFromTouch();
                return false;
            }
        });

        mListView = (MaterialListView) myView.findViewById(R.id.material_listview);
        //this.fileAdapter = new MessageAdapter(myView.getContext(), mListView, getArguments().getString(USERTO),this);

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


        return myView;
    }

    public void update() {
        GetGroupListTask fillGroupList = new GetGroupListTask();
        fillGroupList.execute();
    }

    public void searchGroups() {
        SearchGroups task = new SearchGroups();
        try {
            task.execute();
        } catch (Exception x){
            Toast.makeText(getActivity().getApplicationContext(), "Error al buscar foros.", Toast.LENGTH_SHORT).show();
        }

    }

    public void searchClear(View view) {
        emptyView.setVisibility(View.INVISIBLE);
        searchText.setText("");
        searchGroups();
    }

    private  class SearchGroups extends AsyncTask<Void, Void, List<Forum>> {

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
        protected List<Forum> doInBackground(Void... params) {
            List<Forum> forum = null;
            try {

            } catch (Exception ex) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos de archivos.", Toast.LENGTH_SHORT).show();

            }
            return forum;
        }

        @Override
        protected void onPostExecute(List<Forum> forums) {
            if (forums == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos de archivos.", Toast.LENGTH_SHORT).show();
            } else {
                if(forums.isEmpty())
                    emptyView.setVisibility(View.VISIBLE);
                else
                    emptyView.setVisibility(View.INVISIBLE);

                prgrsBar.setVisibility(View.INVISIBLE);
                //forumAdapter.setForums(forums, session.getUserid());
            }
        }
    }

    private class GetGroupListTask extends AsyncTask<Void, Void,
            List<Forum>> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
        }

        @Override
        protected List<Forum> doInBackground(Void... params) {

            List<Forum> forums = null;
            IApiUser api = restAdapter.create(IApiUser.class);
            try {
                //TODO: traer lista de forums del grupo

            } catch (Exception x) {}

            return forums;
        }

        @Override
        protected void onPostExecute(List<Forum> forums) {

            if(forums!=null) {
               //TODO: volver a cargar la lista de forums
            }
        }
    }

}
