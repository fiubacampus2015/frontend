package com.fiuba.campus2015.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static com.fiuba.campus2015.extras.Constants.COMENTARIO;
import static com.fiuba.campus2015.extras.Constants.NAME;

import com.fiuba.campus2015.R;

/**
 * Created by ismael on 09/04/15.
 */
public class CommentsFragment extends Fragment {
    private EditText card1;
    private EditText card2;
    private boolean disable;


    public static CommentsFragment newInstance(String comentario) {
        CommentsFragment myFragment = new CommentsFragment();

        Bundle args = new Bundle();
        args.putString(COMENTARIO, comentario);
        myFragment.setArguments(args);

        return myFragment;
    }

    public void disable() {
        disable = true;
    }

    private void disableComponents() {
        if(disable)
            card1.setEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.comments, container, false);

        card1 = (EditText)view.findViewById(R.id.info_text1);

        card1.setText(getArguments().getString(COMENTARIO));
        disableComponents();
        return view;
    }

    public Bundle getData() {
        Bundle bundle = new Bundle();

        bundle.putString(COMENTARIO, card1.getText().toString());

        return bundle;
    }
}
