package com.fiuba.campus2015.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.dexafree.materialList.cards.BasicButtonsCard;
import com.dexafree.materialList.cards.BasicImageButtonsCard;
import com.dexafree.materialList.cards.BigImageButtonsCard;
import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.cards.WelcomeCard;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.view.MaterialListView;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.dto.user.Message;
import com.fiuba.campus2015.extras.Constants;
import com.fiuba.campus2015.extras.Utils;
import com.gc.materialdesign.views.CheckBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.fiuba.campus2015.extras.Constants.FECHAINGRESO;
import static com.fiuba.campus2015.extras.Constants.FECHAINGRESOEMPLEO;
import static com.fiuba.campus2015.extras.Utils.stringToCalendar;

public class MessageAdapter {
    private LayoutInflater layoutInflater;
    private List<Message> messageItems;
    private Context context;
    private MaterialListView materialListView;

    public MessageAdapter(Context context, MaterialListView materialListView){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.materialListView = materialListView;
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

    private Card getRandomCard(Message msg) {
        String title = msg.user.name + " " + msg.user.username;
        String description = Utils.getBirthdayFormatted(msg.date) + "\n \n" + msg.content;
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

            case text:
                card = new BasicButtonsCard(this.context);
                card.setDescription(description);
                card.setTitle(title);
                card.setTag("BASIC_BUTTONS_CARD");
                ((BasicButtonsCard) card).setLeftButtonText("");
                ((BasicButtonsCard) card).setRightButtonText("Borrar");
                ((BasicButtonsCard) card).setRightButtonTextColorRes(R.color.accent_material_dark);

                if (position % 2 == 0)
                    ((BasicButtonsCard) card).setDividerVisible(true);

                ((BasicButtonsCard) card).setOnLeftButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(context, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                    }
                });

                ((BasicButtonsCard) card).setOnRightButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(context, "You have pressed the right button", Toast.LENGTH_SHORT).show();
                    }
                });
                card.setDismissible(true);


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

    private Card generateNewCard() {
        SimpleCard card = new BasicImageButtonsCard(context);
        card.setDrawable(R.drawable.ic_action_camera);
        card.setTitle("I'm new");
        card.setDescription("I've been generated on runtime!");
        card.setTag("BASIC_IMAGE_BUTTONS_CARD");

        return card;
    }


}
