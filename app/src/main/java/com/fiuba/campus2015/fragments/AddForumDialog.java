package com.fiuba.campus2015.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Forum;
import com.fiuba.campus2015.dto.user.Group;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.UrlEndpoints;
import com.fiuba.campus2015.services.Application;
import com.fiuba.campus2015.services.IApiUser;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.services.RestServiceAsync;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.views.ButtonFlat;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.squareup.otto.Subscribe;

import retrofit.RestAdapter;
import retrofit.client.Response;

import static com.fiuba.campus2015.extras.Utils.getPhotoString;

public class AddForumDialog extends AlertDialog.Builder {
    private Context context;
    private View dialogView;
    private AlertDialog alertDialog;
    private MaterialEditText forumFirstMsg;
    private MaterialEditText forumTitle;
    private SessionManager session;
    private GroupForumsFragment groupforumFragment;
    private String groupId;

    protected AddForumDialog(FragmentActivity activity, GroupForumsFragment groupforumFragment, String groupId) {
        super(activity);

        this.groupId = groupId;
        this.groupforumFragment = groupforumFragment;
        this.context = activity;
        LayoutInflater inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_forum_layout, null);
        session = new SessionManager(context);

        forumTitle = (MaterialEditText) dialogView.findViewById(R.id.forumTitle);
        forumFirstMsg = (MaterialEditText) dialogView.findViewById(R.id.forumFirstMsg);

        setView(dialogView);

        setListener();

        alertDialog = create();

        Application.getEventBus().register(this);

    }

    public void showDialog() {
        alertDialog.show();
    }


    private boolean validateData() {

        if (isEmpty(forumTitle)) {
            ((MaterialEditText) dialogView.findViewById(R.id.forumTitle)).validateWith(new RegexpValidator("Ingresá un título.", "\\d+"));
            return false;
        }
        return true;
    }

    private boolean isEmpty(TextView textview) {
        return (textview.getText().length() == 0);
    }

    private void setListener() {
        ButtonFlat buttonAccept = (ButtonFlat) dialogView.findViewById(R.id.addForum);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    createForum();
                    groupforumFragment.update();
                    alertDialog.dismiss();
                    reset();
                } else {

                }
            }
        });

        ButtonFlat buttonCancel = (ButtonFlat) dialogView.findViewById(R.id.cancelNewForum);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                reset();
            }
        });

    }

    public void reset() {
        forumTitle.setText("");
        forumFirstMsg.setText("");
    }



    @Subscribe
    public void onResponse(com.fiuba.campus2015.services.Response posts) {


    }


    public void createForum() {



        RestServiceAsync.GetResult result = new RestServiceAsync.GetResult<Response, IApiUser>() {
            @Override
            public Response getResult(IApiUser service) {
                return service.createForum(session.getToken(),groupId,
                new Forum(forumTitle.getText().toString(),
                        new Message(forumFirstMsg.getText().toString(), Constants.MsgCardType.text)));
            }
        };

        RestClient restClient = new RestClient();

        RestServiceAsync callApi = new RestServiceAsync<Response, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new com.fiuba.campus2015.services.Response());

    }
}


