package com.fiuba.campus2015.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Toast;

import com.dexafree.materialList.cards.BasicImageButtonsCard;
import com.dexafree.materialList.cards.BigImageButtonsCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.controller.IMaterialListAdapter;
import com.dexafree.materialList.controller.OnDismissCallback;
import com.dexafree.materialList.controller.SwipeDismissRecyclerViewTouchListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.ForumMessage;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.customcard.BigPhotoCard;
import com.fiuba.campus2015.customcard.PlaceCard;
import com.fiuba.campus2015.customcard.TextCard;
import com.fiuba.campus2015.customcard.LinkCard;
import com.fiuba.campus2015.customcard.VideoCard;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.RecyclerItemClickListener;
import com.fiuba.campus2015.extras.Utils;
import com.fiuba.campus2015.fragments.GroupFilesFragment;
import com.fiuba.campus2015.fragments.WallFragment;
import com.fiuba.campus2015.services.RestClient;
import com.fiuba.campus2015.session.SessionManager;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.ProgressDialog;
import static com.fiuba.campus2015.extras.Constants.MOVIES;
import static com.fiuba.campus2015.extras.Constants.CAMPUS;
import static com.fiuba.campus2015.extras.Constants.MP4;
import static com.fiuba.campus2015.extras.Constants.SEP;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

public class MessageAdapter {
    private LayoutInflater layoutInflater;
    private List<Message> messageItems;
    private Context context;
    private MaterialListView materialListView;
    private String userId;
    private SessionManager session;
    private WallFragment wallFragment;
    private GroupFilesFragment fileFragment;
    private ForumMessage forumMessage;

    public MessageAdapter(Context context, MaterialListView materialListView , String userId, WallFragment fragment){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.materialListView = materialListView;
        this.userId = userId;
        this.session = new SessionManager(context.getApplicationContext());
        this.wallFragment = fragment;

        setDismissCallback();
    }

    public MessageAdapter(Context context, MaterialListView materialListView , String userId, GroupFilesFragment fragment){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.materialListView = materialListView;
        this.userId = userId;
        this.session = new SessionManager(context.getApplicationContext());
        this.fileFragment = fragment;

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

    private String getOwnerId(){

        String ownerId = "";

        if (wallFragment != null) ownerId = wallFragment.getWallUserId();
        if (fileFragment != null) ownerId = fileFragment.getGroupOwnerId();
        if (forumMessage != null) ownerId = forumMessage.getGroupOwnerId();

        return ownerId;
    }

    private Card getRandomCard(Message msg) {
        String title = msg.user.name + " " + msg.user.username;
        String description = Utils.getBirthdayFormatted(msg.date) + "\n \n" + msg.content;

        final String idMessage = msg._id;

        int position = 0;
        SimpleCard card;

        switch (msg.typeOf) {

            case place:
                card = new BasicImageButtonsCard(this.context);
                card.setDescription(Utils.getBirthdayFormatted(msg.date));
                card.setTitle(title);
                Drawable drawablePlace = new BitmapDrawable(context.getResources(), Utils.getPhoto(msg.content));
                card.setDrawable(drawablePlace);
                card.setTag("PLACE_CARD");
                card.setDismissible(true);
                ((BasicImageButtonsCard) card).setLeftButtonText("Borrar");
                ((BasicImageButtonsCard) card).setLeftButtonTextColorRes(R.color.my_awesome_darker_color);
                ((BasicImageButtonsCard) card).setRightButtonText("");
                ((BasicImageButtonsCard) card).setOnLeftButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        deleteCard(idMessage, card);
                    }
                });

                card.setDismissible(true);

                return card;

            case photo:
                card = new BigPhotoCard(this.context, session.getUserid(), msg.user._id, getOwnerId());
                card.setDescription(Utils.getBirthdayFormatted(msg.date));
                card.setTitle(title);
                card.setDismissible(true);

                Drawable drawable = new BitmapDrawable(context.getResources(), Utils.getPhoto(msg.content));
                card.setDrawable(drawable);

                card.setTag("BIG_PHOTO_CARD");

                ((BigPhotoCard)card).setOnButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        deleteCard(idMessage, card);
                    }
                });

                card.setDismissible(((BigPhotoCard) card).isDeleteable());

                return card;

            case video:
                card = new VideoCard(this.context, session.getUserid(), msg.user._id, getOwnerId());
                card.setTitle(title);
                card.setTag("VIDEO_CARD");
                card.setDismissible(true);

                String videoString = msg.content;
                byte[] videoBytes = Base64.decode(videoString.getBytes(), Base64.DEFAULT);

                String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        SEP + CAMPUS + SEP + MOVIES;
                java.util.Date date = new Date();
                String nameFile = Long.toString(date.getTime()) + MP4;
                final String completeName = path + File.separator + nameFile;

                try {
                    // si la carpeta  esta creada no se sobrescribe
                    File folder = new File(path);
                    folder.mkdirs();

                    // se guarda el archivo en la carpeta creada
                    FileOutputStream out = new FileOutputStream(completeName);
                    out.write(videoBytes);
                    out.close();

                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(completeName, MediaStore.Video.Thumbnails.MINI_KIND);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(wallFragment.getResources(), thumb);
                    ((VideoCard) card).setVideoPreview(bitmapDrawable);

                    ((VideoCard)card).setOnButtonPressedListenerPreview(new OnButtonPressListener() {
                        @Override
                        public void onButtonPressedListener(View view, Card card) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(completeName)), "video/*");
                            wallFragment.startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(context, "error al grabar el video", Toast.LENGTH_LONG).show();
                }

                ((VideoCard)card).setOnButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        deleteCard(idMessage, card);
                    }
                });

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
                card = new TextCard(this.context, session.getUserid(), msg.user._id, getOwnerId());
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
                card = new LinkCard(this.context, session.getUserid(), msg.user._id, getOwnerId());
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
/*
        // deshabilita el swipe
        SwipeDismissRecyclerViewTouchListener mDismissListener = new
                SwipeDismissRecyclerViewTouchListener(materialListView,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {

            @Override
            public boolean canDismiss(int i) {
                return false;
            }

            @Override
            public void onDismiss(RecyclerView recyclerView, int[] ints) {
                    }
                });

        materialListView.setOnTouchListener(mDismissListener);
        */

        SwipeDismissRecyclerViewTouchListener listener =
                new SwipeDismissRecyclerViewTouchListener(materialListView, new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    public boolean canDismiss(int position) {
                        return ((IMaterialListAdapter) materialListView.getAdapter()).getCard(position).isDismissible();
                    }

                    public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        int[] arr$ = reverseSortedPositions;
                        int len$ = reverseSortedPositions.length;

                        for(int i$ = 0; i$ < len$; ++i$) {
                            int reverseSortedPosition = arr$[i$];
                            Card card = ((IMaterialListAdapter)materialListView.getAdapter()).getCard(reverseSortedPosition);

                            switch(card.getTag().toString()) {
                                case "TEXT_CARD":
                                    OnButtonPressListener button = ((TextCard)card).getOnButtonPressedListener();
                                    button.onButtonPressedListener(null, card);
                                    break;
                                case "VIDEO_CARD":
                                    ((VideoCard)card).getOnButtonPressedListener().onButtonPressedListener(null,card);
                                    break;
                                case "LINK_CARD":
                                    OnButtonPressListener button2 = ((LinkCard)card).getOnButtonPressedListener();
                                    button2.onButtonPressedListener(null, card);
                                    break;
                                case "BIG_IMAGE_BUTTON_CARD":
                                    OnButtonPressListener button3 = ((BigImageButtonsCard)card).getOnLeftButtonPressedListener();
                                    button3.onButtonPressedListener(null, card);
                                    break;
                                case "PLACE_CARD":
                                    ((BasicImageButtonsCard)card).getOnLeftButtonPressedListener().onButtonPressedListener(null,card);
                                    break;
                                case "BIG_PHOTO_CARD":
                                    ((BigPhotoCard)card).getOnButtonPressedListener().onButtonPressedListener(null,card);
                            }
                        }
                    }
                });

        materialListView.setOnTouchListener(listener);

    }

    private Card generateNewCard() {
        SimpleCard card = new BasicImageButtonsCard(context);
        card.setDrawable(R.drawable.ic_action_camera);
        card.setTitle("I'm new");
        card.setDescription("I've been generated on runtime!");
        card.setTag("BASIC_IMAGE_BUTTONS_CARD");

        return card;
    }

    public void addMsg(Message card){
        materialListView.addAtStart(getRandomCard(card));
    }


    private void deleteCard(final String idMessage, final Card card) {
        Dialog dialogo = new Dialog(context, null, "¿Estás seguro que deseas eliminar el mensaje?");
        dialogo.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteMsgTask deleteMsg = new DeleteMsgTask(idMessage, card);
                deleteMsg.execute();
            }
        });
        dialogo.addCancelButton("Cancelar");
        dialogo.show();
        dialogo.getButtonAccept().setText("Aceptar");
    }

    private class DeleteMsgTask extends AsyncTask<Void, Void,retrofit.client.Response> {
        RestClient restClient;
        private String idMessage;
        private ProgressDialog progressDialog;
        private Card card;

        public DeleteMsgTask(String idMessage, Card card)
        {
            Activity activity = forumMessage;

            if (wallFragment!=null)
                activity = wallFragment.getActivity();
            if (fileFragment!=null)
                activity = fileFragment.getActivity();

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
            else {
                delete(card);
                if(forumMessage != null) {
                    forumMessage.deletedMessage();
                }
            }
        }
    }

}
