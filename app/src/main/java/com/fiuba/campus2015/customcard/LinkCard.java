package com.fiuba.campus2015.customcard;

import android.content.Context;
import android.graphics.Color;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.fiuba.campus2015.R;

public class LinkCard extends SimpleCard {
    private String buttonText;
    private OnButtonPressListener mListener;
    private int dividerColor;
    private int buttonTextColor;
    private boolean showDeleteButton;

    public LinkCard(final Context context, String userId, String msgUserId) {
        super(context);
        buttonText = "Borrar";
        buttonTextColor = Color.parseColor("#ff80cbc4");
        dividerColor = Color.parseColor("#D4D4D4");
        showDeleteButton = (userId.equalsIgnoreCase(msgUserId));
    }

    public boolean isDeleteable(){
        return showDeleteButton;
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

    @Override
    public int getLayout() {
        return R.layout.link_card;
    }
}
