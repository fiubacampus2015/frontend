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

import com.fiuba.campus2015.ProfileFriend;
import com.fiuba.campus2015.ProfileReduced;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.ContactsAdapter;
import com.fiuba.campus2015.adapter.GroupAdapter;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.User;
import com.fiuba.campus2015.extras.RecyclerItemClickListener;
import com.fiuba.campus2015.session.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.fiuba.campus2015.extras.Constants.USER;


public class GroupFragment extends Fragment {
    private View myView;
    private SessionManager session;
    private GroupAdapter groupAdapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar prgrsBar;

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

        groupAdapter.setGroups(new ArrayList<Group>(), session.getUserid());

        return myView;
    }

}
