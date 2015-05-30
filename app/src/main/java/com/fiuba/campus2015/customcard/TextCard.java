package com.fiuba.campus2015.customcard;


import android.content.Context;
import android.graphics.Color;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.fiuba.campus2015.R;

public class TextCard extends SimpleCard {
    private String buttonText;
    private OnButtonPressListener mListener;
    private int dividerColor;
    private int buttonTextColor;
    private boolean showDeleteButton;

    public TextCard(final Context context, String userId, String msgUserId, String wallUserId) {
        super(context);
        buttonText = "Borrar";
        buttonTextColor = Color.parseColor("#ff80cbc4");
        dividerColor = Color.parseColor("#D4D4D4");

        if(wallUserId.equalsIgnoreCase(userId)){
            showDeleteButton = true;
        } else showDeleteButton = (userId.equalsIgnoreCase(msgUserId));
    }

    public boolean isDeleteable(){
        return showDeleteButton;
    }

    @Override
    public int getLayout() {
        return R.layout.text_card;
    }
    public OnButtonPressListener getOnButtonPressedListener() {
        return mListener;
    }

    public void setOnButtonPressedListener(OnButtonPressListener mListener) {
        this.mListener = mListener;
    }

    public String getButtonText() {
        return buttonText;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public int getButtonTextColor() {
        return buttonTextColor;
    }

}
