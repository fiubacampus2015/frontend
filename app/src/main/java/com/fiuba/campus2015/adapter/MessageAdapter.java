package com.fiuba.campus2015.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Toast;

import com.dexafree.materialList.cards.BasicImageButtonsCard;
import com.dexafree.materialList.cards.BigImageButtonsCard;
import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.controller.IMaterialListAdapter;
import com.dexafree.materialList.controller.OnDismissCallback;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.ForumMessage;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.customcard.TextCard;
import com.fiuba.campus2015.customcard.LinkCard;
import com.fiuba.campus2015.customcard.VideoCard;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.fragments.WallFragment;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.ProgressDialog;
import java.util.List;

public class MessageAdapter {
    private LayoutInflater layoutInflater;
    private List<Message> messageItems;
    private Context context;
    private MaterialListView materialListView;
    private String userId;
    private SessionManager session;
    private WallFragment wallFragment;
    private ForumMessage forumMessage;


    public MessageAdapter(Context context, MaterialListView materialListView , String userId, WallFragment wallFragment){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.materialListView = materialListView;
        this.userId = userId;
        this.session = new SessionManager(context.getApplicationContext());
        this.wallFragment = wallFragment;
        setDismissCallback();
    }

    public MessageAdapter(Context context, MaterialListView materialListView , String userId,ForumMessage forumMessage){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.materialListView = materialListView;
        this.userId = userId;
        this.session = new SessionManager(context.getApplicationContext());
        this.forumMessage = forumMessage;

        setDismissCallback();
    }

    // probando video
    public void setVideoTest(Message msg, Drawable videoPreview, final Uri uriVideo) {
        VideoCard card = new VideoCard(context);
        card.setTitle(msg.user.name + " " + msg.user.username);
        card.setDescription("\n" + msg.content);
        card.setTag("VIDEO_CARD");
        card.setVideoPreview(videoPreview);
        card.setDismissible(true);

        card.setOnButtonPressedListenerPreview(new OnButtonPressListener() {
            @Override
            public void onButtonPressedListener(View view, Card card) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uriVideo, "video/*");
                wallFragment.startActivity(intent);
            }
        });

        card.setOnButtonPressedListener(new OnButtonPressListener() {
            @Override
            public void onButtonPressedListener(View view, Card card) {
             delete(card);
            }
        });

        materialListView.addAtStart(card);
    }

    public void fillArray() {
        materialListView.clear();
        for (int i = 0; i < this.messageItems.size(); i++) {
            Card card = getRandomCard(this.messageItems.get(i));
            materialListView.add(card);
        }
    }

    public void setData(List<Message> msgs) {
        this.messageItems = msgs;
    }

    public void delete(Card card) {
        ((IMaterialListAdapter)materialListView.getAdapter()).remove(card,false);
    }

    private Card getRandomCard(Message msg) {
        String title = msg.user.name + " " + msg.user.username;
        String description = Utils.getBirthdayFormatted(msg.date) + "\n \n" + msg.content;

        final String idMessage = msg._id;

        int position = 0;
        SimpleCard card;

        switch (msg.typeOf) {

            case place:
                card = new SmallImageCard(this.context);
                card.setDescription(description);
                card.setTitle(title);
                card.setDrawable(R.drawable.icon_location_wall);
                card.setDismissible(true);
                card.setTag("SMALL_IMAGE_CARD");
                return card;

            case photo:
                card = new BigImageCard(this.context);
                card.setDescription(description);
                card.setTitle(title);
                //card.setDrawable(R.drawable.photo);
                card.setDrawable("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
                card.setTag("BIG_IMAGE_CARD");
                card.setDismissible(true);
                return card;

            case video:
                card = new BasicImageButtonsCard(this.context);
                card.setDescription(description);
                card.setTitle(title);
                //card.setDrawable(R.drawable.msg);
                card.setTag("BASIC_IMAGE_BUTTON_CARD");
                ((BasicImageButtonsCard) card).setLeftButtonText("LEFT");
                ((BasicImageButtonsCard) card).setRightButtonText("RIGHT");

                if (position % 2 == 0)
                    ((BasicImageButtonsCard) card).setDividerVisible(true);

                ((BasicImageButtonsCard) card).setOnLeftButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(context, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                        ((SimpleCard) card).setTitle("CHANGED ON RUNTIME");
                    }
                });

                ((BasicImageButtonsCard) card).setOnRightButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(context, "You have pressed the right button on card " + ((SimpleCard) card).getTitle(), Toast.LENGTH_SHORT).show();
                        materialListView.remove(card);
                    }
                });
                card.setDismissible(true);

                return card;

            case file:
                card = new BasicImageButtonsCard(this.context);
                card.setDescription(description);
                card.setTitle(title);
                card.setTag("BASIC_IMAGE_BUTTON_CARD");
                ((BasicImageButtonsCard) card).setLeftButtonText("LEFT");
                ((BasicImageButtonsCard) card).setRightButtonText("RIGHT");

                if (position % 2 == 0)
                    ((BasicImageButtonsCard) card).setDividerVisible(true);

                ((BasicImageButtonsCard) card).setOnLeftButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(context, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                        ((SimpleCard) card).setTitle("CHANGED ON RUNTIME");
                    }
                });

                ((BasicImageButtonsCard) card).setOnRightButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(context, "You have pressed the right button on card " + ((SimpleCard) card).getTitle(), Toast.LENGTH_SHORT).show();
                        materialListView.remove(card);
                    }
                });
                card.setDismissible(true);

                return card;

            case text:
                card = new TextCard(this.context, session.getUserid(), msg.user._id, (wallFragment == null? "": wallFragment.getWallUserId()));
                card.setDescription(description);
                card.setTitle(title);
                card.setTag("TEXT_CARD");

                ((TextCard)card).setOnButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        deleteCard(idMessage, card);
                    }
                });

                card.setDismissible(((TextCard) card).isDeleteable());
                return card;

            case link:
                card = new LinkCard(this.context, session.getUserid(), msg.user._id, (wallFragment == null? "": wallFragment.getWallUserId()));
                card.setDescription(description);
                card.setTitle(title);
                card.setTag("LINK_CARD");
                card.setDismissible(((LinkCard) card).isDeleteable());


                ((LinkCard)card).setOnButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        deleteCard(idMessage, card);
                    }
                });

                return card;

          /*  case place:
                card = new WelcomeCard(context);
                card.setTitle("Welcome Card");
                card.setDescription("I am the description");
                card.setTag("WELCOME_CARD");
                ((WelcomeCard) card).setSubtitle("My subtitle!");
                ((WelcomeCard) card).setButtonText("Okay!");
                ((WelcomeCard) card).setOnButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show();
                    }
                });

                if (position % 2 == 0)
                    ((WelcomeCard) card).setBackgroundColorRes(R.color.background_material_dark);
                card.setDismissible(true);

                return card;*/

            default:
                card = new BigImageButtonsCard(context);
                card.setDescription(description);
                card.setTitle(title);
                card.setDrawable(R.drawable.wallpaper);
                card.setTag("BIG_IMAGE_BUTTONS_CARD");
                ((BigImageButtonsCard) card).setLeftButtonText("ADD CARD");
                ((BigImageButtonsCard) card).setRightButtonText("RIGHT BUTTON");

                if (position % 2 == 0) {
                    ((BigImageButtonsCard) card).setDividerVisible(true);
                }

                ((BigImageButtonsCard) card).setOnLeftButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Log.d("ADDING", "CARD");

                        materialListView.add(generateNewCard());
                        Toast.makeText(context, "Added new card", Toast.LENGTH_SHORT).show();
                    }
                });

                ((BigImageButtonsCard) card).setOnRightButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(context, "You have pressed the right button", Toast.LENGTH_SHORT).show();
                    }
                });
                card.setDismissible(true);


                return card;

        }}

    private void setDismissCallback() {
        OnDismissCallback callback = new OnDismissCallback() {
            @Override
            public void onDismiss(Card card, int i) {
                switch(card.getTag().toString()) {
                    case "TEXT_CARD":
                        OnButtonPressListener button = ((TextCard)card).getOnButtonPressedListener();
                        button.onButtonPressedListener(null, card);
                        break;
                    case "VIDEO_CARD":
                        break;
                    case "LINK_CARD":
                        OnButtonPressListener button2 = ((LinkCard)card).getOnButtonPressedListener();
                        button2.onButtonPressedListener(null, card);
                        break;
                }
            }
        };
        materialListView.setOnDismissCallback(callback);
    }

    private Card generateNewCard() {
        SimpleCard card = new BasicImageButtonsCard(context);
        card.setDrawable(R.drawable.ic_action_camera);
        card.setTitle("I'm new");
        card.setDescription("I've been generated on runtime!");
        card.setTag("BASIC_IMAGE_BUTTONS_CARD");

        return card;
    }

    private void deleteCard(String idMessage, Card card) {
        DeleteMsgTask deleteMsg = new DeleteMsgTask(idMessage, card);
        deleteMsg.execute();
    }

    private class DeleteMsgTask extends AsyncTask<Void, Void,retrofit.client.Response> {
        RestClient restClient;
        private String idMessage;
        private ProgressDialog progressDialog;
        private Card card;

        public DeleteMsgTask(String idMessage, Card card)
        {
            Activity activity;
            if (wallFragment!=null)
                activity = wallFragment.getActivity();
            else
                activity = forumMessage;


            progressDialog =  new ProgressDialog(activity, "Borrando mensaje.");            this.idMessage = idMessage;
            this.card = card;
        }

        public void executeTask() {
            try {
                this.execute();
            } catch (Exception e) {
                Toast.makeText(context.getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            restClient = new RestClient();
            progressDialog.show();
            }

            @Override
            protected retrofit.client.Response doInBackground(Void... params) {
                retrofit.client.Response response = null;
                try {


                    if (wallFragment != null)
                        response = restClient.getApiService().deleteMsg(session.getToken(), userId,new Message(idMessage));
                    else
                        response = restClient.getApiService().deleteForumMessage(session.getToken(), forumMessage.getGroupId(), forumMessage.getForumId(), new Message(idMessage));

                } catch (Exception ex) {

                }
                return response;
            }


            @Override
        public void onPostExecute(retrofit.client.Response response) {
                progressDialog.dismiss();
            if (response== null)
                Toast.makeText(context.getApplicationContext(), "Hubo un error al borrar el mensaje.", Toast.LENGTH_SHORT).show();
            else
                delete(card);
            //wallFragment.update();

        }
    }

}
