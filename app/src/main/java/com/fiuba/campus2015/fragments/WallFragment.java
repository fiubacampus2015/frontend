package com.fiuba.campus2015.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dexafree.materialList.cards.BasicButtonsCard;
import com.dexafree.materialList.cards.BasicImageButtonsCard;
import com.dexafree.materialList.cards.BasicListCard;
import com.dexafree.materialList.cards.BigImageButtonsCard;
import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.cards.WelcomeCard;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.adapter.MessageAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.jar.Attributes;


public class WallFragment extends Fragment
{
    private View myView;
    private MaterialListView mListView;


    public static WallFragment newInstance(String param1, String param2) {
        WallFragment fragment = new WallFragment();
        Bundle args = new Bundle();
        //put any extra arguments that you may want to supply to this fragment
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.wall_fragment, container, false);

        FloatingActionButton button_actionAddPhoto = (FloatingActionButton) myView.findViewById(R.id.action_addphoto);
        button_actionAddPhoto.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddPhoto.setColorNormalResId(R.color.accent);
        button_actionAddPhoto.setColorPressedResId(R.color.black);
        button_actionAddPhoto.setIcon(R.drawable.ic_action_camera);
        button_actionAddPhoto.setStrokeVisible(false);

        FloatingActionButton button_actionAddMeg = (FloatingActionButton) myView.findViewById(R.id.action_write);
        button_actionAddMeg.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddMeg.setColorNormalResId(R.color.accent);
        button_actionAddMeg.setColorPressedResId(R.color.black);
        button_actionAddMeg.setIcon(R.drawable.ic_comment_grey);
        button_actionAddMeg.setStrokeVisible(false);

        FloatingActionButton button_actionAddPlace = (FloatingActionButton) myView.findViewById(R.id.action_addPlace);
        button_actionAddPlace.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddPlace.setColorNormalResId(R.color.accent);
        button_actionAddPlace.setColorPressedResId(R.color.black);
        button_actionAddPlace.setIcon(R.drawable.ic_film_grey);
        button_actionAddPlace.setStrokeVisible(false);

        FloatingActionButton button_actionAddVideo = (FloatingActionButton) myView.findViewById(R.id.action_addVideo);
        button_actionAddVideo.setSize(FloatingActionButton.SIZE_MINI);
        button_actionAddVideo.setColorNormalResId(R.color.accent);
        button_actionAddVideo.setColorPressedResId(R.color.black);
        button_actionAddVideo.setIcon(R.drawable.ic_location_on_grey600_18dp);
        button_actionAddVideo.setStrokeVisible(false);

        //final View actionAddPhoto = myView.findViewById(R.id.action_addphoto);

       /* FloatingActionButton actionAddMsg = (FloatingActionButton) myView.findViewById(R.id.action_write);
        actionAddMsg.setTitle("Hide/Show Action above");
        actionAddMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionAddPhoto.setVisibility(actionAddPhoto.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        final FloatingActionButton actionAddVideo = (FloatingActionButton) myView.findViewById(R.id.action_addVideo);
        actionAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionAddVideo.setTitle("Action A clicked");
            }
        });*/


        mListView = (MaterialListView) myView.findViewById(R.id.material_listview);

        // Fill the array with mock content
        fillArray();


        return myView;
    }

    private void fillArray() {
        for (int i = 0; i < 35; i++) {
            Card card = getRandomCard(i);
            mListView.add(card);
        }
    }

    private Card getRandomCard(final int position) {
        String title = "Card number " + (position + 1);
        String description = "Lorem ipsum dolor sit amet";

        int type = position % 6;

        SimpleCard card;
        Drawable icon;

        switch (type) {

            case 0:
                card = new SmallImageCard(this.myView.getContext());
                card.setDescription(description);
                card.setTitle(title);
                card.setDrawable(R.drawable.ic_launcher);
                card.setDismissible(true);
                card.setTag("SMALL_IMAGE_CARD");
                return card;

            case 1:
                card = new BigImageCard(this.myView.getContext());
                card.setDescription(description);
                card.setTitle(title);
                //card.setDrawable(R.drawable.photo);
                card.setDrawable("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
                card.setTag("BIG_IMAGE_CARD");
                return card;

            case 2:
                card = new BasicImageButtonsCard(this.myView.getContext());
                card.setDescription(description);
                card.setTitle(title);
                card.setDrawable(R.drawable.ic_cake_grey600_24dp);
                card.setTag("BASIC_IMAGE_BUTTON_CARD");
                ((BasicImageButtonsCard) card).setLeftButtonText("LEFT");
                ((BasicImageButtonsCard) card).setRightButtonText("RIGHT");

                if (position % 2 == 0)
                    ((BasicImageButtonsCard) card).setDividerVisible(true);

                ((BasicImageButtonsCard) card).setOnLeftButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(myView.getContext(), "You have pressed the left button", Toast.LENGTH_SHORT).show();
                        ((SimpleCard) card).setTitle("CHANGED ON RUNTIME");
                    }
                });

                ((BasicImageButtonsCard) card).setOnRightButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(myView.getContext(), "You have pressed the right button on card " + ((SimpleCard) card).getTitle(), Toast.LENGTH_SHORT).show();
                        mListView.remove(card);
                    }
                });
                card.setDismissible(true);

                return card;

            case 3:
                card = new BasicButtonsCard(this.myView.getContext());
                card.setDescription(description);
                card.setTitle(title);
                card.setTag("BASIC_BUTTONS_CARD");
                ((BasicButtonsCard) card).setLeftButtonText("LEFT");
                ((BasicButtonsCard) card).setRightButtonText("RIGHT");
                ((BasicButtonsCard) card).setRightButtonTextColorRes(R.color.accent_material_dark);

                if (position % 2 == 0)
                    ((BasicButtonsCard) card).setDividerVisible(true);

                ((BasicButtonsCard) card).setOnLeftButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(myView.getContext(), "You have pressed the left button", Toast.LENGTH_SHORT).show();
                    }
                });

                ((BasicButtonsCard) card).setOnRightButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(myView.getContext(), "You have pressed the right button", Toast.LENGTH_SHORT).show();
                    }
                });
                card.setDismissible(true);


                return card;

            case 4:
                card = new WelcomeCard(this.myView.getContext());
                card.setTitle("Welcome Card");
                card.setDescription("I am the description");
                card.setTag("WELCOME_CARD");
                ((WelcomeCard) card).setSubtitle("My subtitle!");
                ((WelcomeCard) card).setButtonText("Okay!");
                ((WelcomeCard) card).setOnButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(myView.getContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                    }
                });

                if (position % 2 == 0)
                    ((WelcomeCard) card).setBackgroundColorRes(R.color.background_material_dark);
                card.setDismissible(true);

                return card;

            default:
                card = new BigImageButtonsCard(this.myView.getContext());
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

                        mListView.add(generateNewCard());
                        Toast.makeText(myView.getContext(), "Added new card", Toast.LENGTH_SHORT).show();
                    }
                });

                ((BigImageButtonsCard) card).setOnRightButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(myView.getContext(), "You have pressed the right button", Toast.LENGTH_SHORT).show();
                    }
                });
                card.setDismissible(true);


                return card;

        }}

        private Card generateNewCard() {
            SimpleCard card = new BasicImageButtonsCard(this.myView.getContext());
            card.setDrawable(R.drawable.ic_action_camera);
            card.setTitle("I'm new");
            card.setDescription("I've been generated on runtime!");
            card.setTag("BASIC_IMAGE_BUTTONS_CARD");

            return card;
        }

    }

