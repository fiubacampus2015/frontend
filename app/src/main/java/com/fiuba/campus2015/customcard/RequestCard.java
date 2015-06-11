package com.fiuba.campus2015.customcard;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.fiuba.campus2015.R;

public class RequestCard extends SimpleCard {
    private String subtitle;
    private String buttonText;
    private OnButtonPressListener listenerAcceptRequest;
    private OnButtonPressListener listenerReject;
    private int dividerColor;
    private int buttonTextColor;
    private Drawable photoUser;

    public RequestCard(final Context context) {
        super(context);

        buttonText = "Borrar";
        buttonTextColor = Color.parseColor("#ff80cbc4");
        dividerColor = Color.parseColor("#D4D4D4");

        photoUser = context.getResources().getDrawable(R.drawable.ic_account_circle_grey_48dp);
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(int subtitleId) {
        setSubtitle(getString(subtitleId));
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }


    public OnButtonPressListener getOnButtonPressedListenerReject() {
        return listenerReject;
    }
    public OnButtonPressListener getOnButtonPressedListenerAccept() {
        return listenerAcceptRequest;
    }

    public void setOnButtonPressedAccpetListener(OnButtonPressListener listener) {
        listenerAcceptRequest = listener;
    }

    public void setOnButtonPressedRejectListener(OnButtonPressListener listener) {
        listenerReject = listener;
    }


    public OnButtonPressListener getOnButtonPressedListener() {
        return listenerAcceptRequest;
    }

    public void setOnButtonPressedListener(OnButtonPressListener mListener) {
        this.listenerAcceptRequest = mListener;
    }

    public Drawable getPreview() { return photoUser;}

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
        return R.layout.request_card;
    }


}
