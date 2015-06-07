package com.fiuba.campus2015.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import com.fiuba.campus2015.GroupBoard;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.GroupAdapter;
import com.fiuba.campus2015.dto.user.Action;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.MemberShip;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;
import com.fiuba.campus2015.extras.RecyclerItemClickListener;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.Response;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.Dialog;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.fiuba.campus2015.extras.Constants.GROUP;

public class GroupFragment extends Fragment {
    private View myView;
    private SessionManager session;
    private GroupAdapter groupAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar prgrsBar;
    private EditText searchText;
    private AddGroupDialog addGroupDialog;

    public static GroupFragment newInstance(String string1, String string2) {
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
                search();
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        groupAdapter = new GroupAdapter(getActivity());
        recyclerView.setAdapter(groupAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Group group = groupAdapter.getGroup(position);

                        if (group.actions.get(0).action.equals("suscribe"))
                        {
                            Dialog dialog2 = new Dialog(getActivity(), null, "Para poder ingresar a " + group.name + " primero tenés que unirte.");
                            dialog2.show();
                            dialog2.getButtonAccept().setText("Aceptar");
                        }else
                        {
                            Intent intent;
                            intent = new Intent(getActivity(), GroupBoard.class);
                            intent.putExtra(GROUP, new Gson().toJson(group));
                            startActivity(intent);
                        }

                    }

                    @Override public void onItemLongClick(View view, int position) {
                        Group group = groupAdapter.getGroup(position);
                        optionsGroup(group);
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

        List<Group> groupItems = new ArrayList<Group>();

        groupAdapter.setGroups(groupItems, session.getUserid());


        update();
        ButtonFloatMaterial addGroupbutton = (ButtonFloatMaterial) myView.findViewById(R.id.addGroup);
        addGroupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroupDialog.showDialog();
            }
        });

        return myView;
    }


    private void optionsGroup(final Group group)
    {

        final List<String> listItems = new ArrayList<String>();

        for (int i = 0; i < group.actions.size(); i++) {
            Action  action=group.actions.get(i);

            if (action.action.equals("delete"))
                listItems.add("Eliminar grupo.");
            if (action.action.equals("unsuscribe"))
                listItems.add("Abandonar grupo.");
            if (action.action.equals("suscribe"))
                listItems.add("Unirme al grupo.");

        }
        if (!listItems.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Opciones del grupo");
            final CharSequence options[] = listItems.toArray(new CharSequence[listItems.size()]);

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (options[which].equals("Eliminar grupo.")) {
                        Dialog dialog2 = new Dialog(getActivity(), null, "Estás seguro de eliminar grupo?");
                        dialog2.setOnAcceptButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteGroup(group._id);
                                update();
                            }
                        });
                        dialog2.addCancelButton("Cancelar");
                        dialog2.show();
                        dialog2.getButtonAccept().setText("Aceptar");
                    }else if (options[which].equals("Unirme al grupo."))
                    {
                        subscribeGroup(group._id);

                    }else if (options[which].equals("Abandonar grupo."))
                    {
                        Dialog dialogo = new Dialog(getActivity(),null, "¿Estás seguro de abandonar el grupo?");
                        dialogo.setOnAcceptButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                unSubscribeGroup(group._id);
                            }
                        });
                        dialogo.addCancelButton("Cancelar");
                        dialogo.show();
                        dialogo.getButtonAccept().setText("Aceptar");
                    }
                }
            });
            builder.show();
        }
    }

    public void update() {
        searchText.setText("");
        SearchGroups fillGroupList = new SearchGroups();
        fillGroupList.execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        addGroupDialog.onActivityResult(requestCode, resultCode, data);
    }

    public void search() {
        SearchGroups task = new SearchGroups();
        try {
            task.execute();
        } catch (Exception x){
            Toast.makeText(getActivity().getApplicationContext(), "Error al buscar grupos.", Toast.LENGTH_SHORT).show();
        }

    }

    public void searchClear(View view) {
        emptyView.setVisibility(view.INVISIBLE);
        searchText.setText("");
        search();
    }

    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onResponse(retrofit.client.Response response) {
        Application.getEventBus().unregister(this);
        prgrsBar.setVisibility(View.GONE);
    }



    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onSuscription(MemberShip memberShip) {
        if (!memberShip.status.equals("pending"))
        {
            Dialog dialog2 = new Dialog(getActivity(), null, "Te uniste al grupo.");
            dialog2.show();
            dialog2.getButtonAccept().setText("Aceptar");

        }else
        {
            Dialog dialog2 = new Dialog(getActivity(), null, "Enviamo tu solicitud al moderador del grupo.");
            dialog2.show();
            dialog2.getButtonAccept().setText("Aceptar");
        }
        Application.getEventBus().unregister(this);
        prgrsBar.setVisibility(View.GONE);
        update();
    }

    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onErrorResponse(Response errorResponse) {
        Toast.makeText(getActivity().getApplicationContext(), "Hubo un error en grupos." + errorResponse.reason, Toast.LENGTH_SHORT).show();
        prgrsBar.setVisibility(View.GONE);
        Application.getEventBus().unregister(this);
    }

    public void deleteGroup(final String groupId)
    {
        prgrsBar.setVisibility(View.VISIBLE);
        Application.getEventBus().register(this);
        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<retrofit.client.Response, IApiUser>() {
            @Override
            public retrofit.client.Response getResult(IApiUser service) {
                return service.deleteGroup(session.getToken(), new Group(groupId));
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<retrofit.client.Response, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }


    private void subscribeGroup(final String groupId)
    {
        prgrsBar.setVisibility(View.VISIBLE);

        Application.getEventBus().register(this);
        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<MemberShip, IApiUser>() {
            @Override
            public MemberShip getResult(IApiUser service) {
                return service.subscribeGroup(session.getToken(), groupId, new User(session.getUserid()));
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<MemberShip, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }

    private void unSubscribeGroup(final String groupId)
    {
        prgrsBar.setVisibility(View.VISIBLE);
        Application.getEventBus().register(this);
        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<retrofit.client.Response, IApiUser>() {
            @Override
            public retrofit.client.Response getResult(IApiUser service) {
                return service.unSubscribeGroup(session.getToken(), groupId, new User(session.getUserid()));
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<retrofit.client.Response, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
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

                group = restClient.getApiService().getGroup(session.getToken(),searchText.getText().toString());
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

}
