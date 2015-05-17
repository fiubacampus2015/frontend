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

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.GroupAdapter;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.extras.RecyclerItemClickListener;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;

public class GroupFragment extends Fragment {
    private View myView;
    private SessionManager session;
    private GroupAdapter groupAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar prgrsBar;
    private EditText searchText;
    private AddGroupDialog addGroupDialog;

    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.group_fragment, container, false);
        session = new SessionManager(getActivity().getApplicationContext());
        addGroupDialog = new AddGroupDialog(getActivity(), this);

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

        prgrsBar = (ProgressBar) myView.findViewById(R.id.progressBarCircularIndeterminateGroup);
        emptyView = (TextView) myView.findViewById(R.id.empty_view_group);


        recyclerView = (RecyclerView) myView.findViewById(R.id.listViewGroups);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        groupAdapter = new GroupAdapter(getActivity());
        recyclerView.setAdapter(groupAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Group group = groupAdapter.getGroup(position);
                        Intent intent;
                    }
                })
        );

        //Se agrega esto por que sino no funciona el input con tabs
        searchText = (EditText) myView.findViewById(R.id.search_text);
        searchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                searchText.requestFocusFromTouch();
                return false;
            }
        });

        groupAdapter.setGroups(new ArrayList<Group>(), session.getUserid());

        ButtonFloatMaterial addGroupbutton = (ButtonFloatMaterial) myView.findViewById(R.id.addGroup);
        addGroupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroupDialog.showDialog();
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
            Toast.makeText(getActivity().getApplicationContext(), "Error al buscar grupos.", Toast.LENGTH_SHORT).show();
        }

    }

    public void searchClear(View view) {
        emptyView.setVisibility(View.INVISIBLE);
        searchText.setText("");
        searchGroups();
    }

    private  class SearchGroups extends AsyncTask<Void, Void, List<Group>> {

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
        protected List<Group> doInBackground(Void... params) {
            List<Group> group = null;
            try {

            } catch (Exception ex) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos de grupos.", Toast.LENGTH_SHORT).show();

            }
            return group;
        }

        @Override
        protected void onPostExecute(List<Group> groups) {
            if (groups == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos del grupo.", Toast.LENGTH_SHORT).show();
            } else {
                if(groups.isEmpty())
                    emptyView.setVisibility(View.VISIBLE);
                else
                    emptyView.setVisibility(View.INVISIBLE);

                prgrsBar.setVisibility(View.INVISIBLE);
                groupAdapter.setGroups(groups, session.getUserid());
            }
        }
    }

    private class GetGroupListTask extends AsyncTask<Void, Void,
            List<Group>> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UrlEndpoints.URL_API)
                    .build();
        }

        @Override
        protected List<Group> doInBackground(Void... params) {

            List<Group> groups = null;
            IApiUser api = restAdapter.create(IApiUser.class);
            try {
                //TODO: traer lista de grupos del usuario

            } catch (Exception x) {}

            return groups;
        }

        @Override
        protected void onPostExecute(List<Group> groups) {

            if(groups!=null) {
               //TODO: volver a cargar la lista de grupos
            }
        }
    }

}
