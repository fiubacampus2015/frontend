package com.fiuba.campus2015.customcard;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.dexafree.materialList.cards.ExtendedCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.events.BusProvider;
import com.fiuba.campus2015.R;

public class VideoCard extends SimpleCard {
    private String subtitle;
    private String buttonText;
    private OnButtonPressListener mListener;
    private OnButtonPressListener listenerPreview;
    private int dividerColor;
    private int buttonTextColor;
    private Drawable previewVideo;

    public VideoCard(final Context context) {
        super(context);
        buttonText = "Borrar";
        buttonTextColor = Color.parseColor("#ff80cbc4");
        dividerColor = Color.parseColor("#D4D4D4");
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


    public OnButtonPressListener getOnButtonPressedListenerPreview() {
        return listenerPreview;
    }

    public void setOnButtonPressedListenerPreview(OnButtonPressListener listenerPreview) {
        this.listenerPreview = listenerPreview;
    }


    public OnButtonPressListener getOnButtonPressedListener() {
        return mListener;
    }

    public void setOnButtonPressedListener(OnButtonPressListener mListener) {
        this.mListener = mListener;
    }

    public Drawable getPreview() { return previewVideo;}
    public void setVideoPreview(Drawable drawable) { previewVideo = drawable;}
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
        return R.layout.video_card;
    }
}
