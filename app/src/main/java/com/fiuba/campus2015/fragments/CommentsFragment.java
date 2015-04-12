package com.fiuba.campus2015.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static com.fiuba.campus2015.extras.Constants.COMENTARIO;
import com.fiuba.campus2015.R;

/**
 * Created by ismael on 09/04/15.
 */
public class CommentsFragment extends Fragment {
    private EditText card1;
    private EditText card2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.comments, container, false);

        card1 = (EditText)view.findViewById(R.id.info_text1);
        card2 = (EditText)view.findViewById(R.id.info_text2);

        return view;
    }

    public Bundle getData() {
        Bundle bundle = new Bundle();

        bundle.putString(COMENTARIO + "1", card1.getText().toString());
        bundle.putString(COMENTARIO + "2", card2.getText().toString());

        return bundle;
    }
}
