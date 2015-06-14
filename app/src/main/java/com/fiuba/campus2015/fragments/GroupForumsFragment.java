package com.fiuba.campus2015.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.fiuba.campus2015.ForumMessage;
import com.fiuba.campus2015.ProfileReduced;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.ForumAdapter;
import com.fiuba.campus2015.dto.user.Action;
import com.fiuba.campus2015.dto.user.Forum;
import com.fiuba.campus2015.dto.user.Group;
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
import static com.fiuba.campus2015.extras.Constants.FORUM;
import static com.fiuba.campus2015.extras.Constants.GROUPOWNER;

public class GroupForumsFragment extends Fragment {
    private View myView;
    private SessionManager session;
    private ForumAdapter forumAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar prgrsBar;
    private EditText searchText;
    private String groupId;

    private AddForumDialog addForumDialog;

    public static GroupForumsFragment newInstance(Group group) {
        GroupForumsFragment fragment = new GroupForumsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        args.putString(GROUP, group._id);
        args.putString(GROUPOWNER, group.owner._id);

        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.group_forums_fragment, container, false);

        groupId = getArguments().getString(GROUP);
        session = new SessionManager(getActivity().getApplicationContext());
        addForumDialog = new AddForumDialog(getActivity(), this, groupId);

        ImageView buttonSearch = (ImageView) myView.findViewById(R.id.search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        ImageView buttonClear= (ImageView) myView.findViewById(R.id.search_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
                update();
            }
        });

        prgrsBar = (ProgressBar) myView.findViewById(R.id.progressBarCircularIndeterminateForum);
        emptyView = (TextView) myView.findViewById(R.id.empty_view_forums);


        recyclerView = (RecyclerView) myView.findViewById(R.id.listViewForums);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        forumAdapter = new ForumAdapter(getActivity());
        recyclerView.setAdapter(forumAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Forum forum = forumAdapter.getForum(position);

                        if (!forum.suspend)
                        {
                            Intent intent;
                            intent = new Intent(getActivity(), ForumMessage.class);
                            intent.putExtra(FORUM, new Gson().toJson(forum));
                            intent.putExtra(GROUP, groupId);
                            startActivity(intent);
                        }else
                        {
                            Dialog  dialog  = new Dialog(getActivity(), null, "El foro " + forum.title + " está suspendido." +
                                    "\n" + "Por favor, comunicate con el administrador.");
                            dialog.show();
                            dialog.getButtonAccept().setText("Aceptar");

                        }
                    }

                    @Override public void onItemLongClick(View view, int position) {
                        Forum forum = forumAdapter.getForum(position);
                        if (!forum.suspend)
                            optionsForum(forum);

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

        forumAdapter.setForums(new ArrayList<Forum>(), session.getUserid());

        ButtonFloatMaterial addForumbutton = (ButtonFloatMaterial) myView.findViewById(R.id.addForum);
        addForumbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addForumDialog.showDialog();
            }
        });

        update();

        return myView;
    }


    @Override
    public void onStart() {
        super.onStart();

    }


    private void optionsForum(final Forum forum)
    {
        final List<String> listItems = new ArrayList<String>();

        if (forum.owner._id.equals(session.getUserid()) || getArguments().getString(GROUPOWNER).equals(session.getUserid()))
        {
            listItems.add("Eliminar grupo.");
        }
      //  for (int i = 0; i < forum.actions.size(); i++) {
        //    Action action=forum.actions.get(i);
            //if (action.action.equals("delete"))
          //      listItems.add("Eliminar grupo.");
        //}
        if (!listItems.isEmpty()) {

            CharSequence opciones[] = new CharSequence[]{"Eliminar foro"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Opciones del foro");
            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Dialog dialog2 = new Dialog(getActivity(), null, "Estás seguro que desea eliminar foro?");
                    dialog2.setOnAcceptButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Application.getEventBus().register(this);
                            deleteForum(forum._id);
                            update();
                        }
                    });
                    dialog2.addCancelButton("Cancelar");
                    dialog2.show();
                    dialog2.getButtonAccept().setText("Aceptar");
                }
            });
            builder.show();
        }
    }

    public void update() {
        prgrsBar.setVisibility(View.VISIBLE);
        //Suscripcion a los eventos que devuelve el cliente que llama la api
        getForums();
    }


    //Se llama a este metodo en caso de que no haya error
    @Subscribe
    public void onForumList(ArrayList<Forum> forums) {

        if (!forums.isEmpty() && forums.get(0) instanceof Forum)
        {
            forumAdapter.setForums(forums,session.getUserid());
            emptyView.setVisibility(View.INVISIBLE);
            Application.getEventBus().unregister(this);

        }else if (forums.isEmpty())
        {
            emptyView.setVisibility(View.VISIBLE);
            Application.getEventBus().unregister(this);

        }

        prgrsBar.setVisibility(View.GONE);
        //Desuscripcion a los eventos que devuelve el cliente que llama la api
    }


    //Se llama a este metodo en caso de que la api devuelva cualquier tipo de error
    @Subscribe
    public void onResponse(Response responseError) {
        Application.getEventBus().unregister(this);

        Toast.makeText(getActivity().getApplicationContext(), "Hubo un error al obtener los datos del foro." + responseError.reason, Toast.LENGTH_SHORT).show();
        prgrsBar.setVisibility(View.GONE);

    }


    public void getForums()
    {
        Application.getEventBus().register(this);
        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<List<Forum>, IApiUser>() {
            @Override
            public List<Forum> getResult(IApiUser service) {
                return service.getForum(session.getToken(),groupId,searchText.getText().toString());
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<List<Forum>, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }


    public void deleteForum(final String forumId)
    {
        //Se crea la llamada al servicio
        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<retrofit.client.Response, IApiUser>() {
            @Override
            public retrofit.client.Response getResult(IApiUser service) {
                return service.deleteForum(session.getToken(), groupId, new Forum(forumId));
            }
        };

        //Se llama a la api
        RestClient restClient = new RestClient();
        RestServiceAsync callApi = new RestServiceAsync<retrofit.client.Response, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new Response());
    }
}
