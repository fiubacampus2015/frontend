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
    private int subtitleColor = Color.BLACK;
    private int dividerColor = Color.parseColor("#608DFA");
    private int buttonTextColor = Color.WHITE;
    private Drawable previewVideo;

    public VideoCard(final Context context) {
        super(context);
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

    public int getSubtitleColor() {
        return subtitleColor;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public int getButtonTextColor() {
        return buttonTextColor;
    }

    public void setButtonText(int buttonTextId) {
        setButtonText(getString(buttonTextId));
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setSubtitleColorRes(int colorId) {
        setSubtitleColor(getResources().getColor(colorId));
    }

    public void setSubtitleColor(int color) {
        this.subtitleColor = color;
    }

    public void setSubtitleColor(String color) {
        setSubtitleColor(Color.parseColor(color));
    }

    public void setDividerColorRes(int colorId) {
        setDividerColor(getResources().getColor(colorId));
    }

    public void setDividerColor(int color) {
        this.dividerColor = color;
    }

    public void setDividerColor(String color) {
        setDividerColor(Color.parseColor(color));
    }

    public void setButtonTextColorRes(int colorId) {
        setButtonTextColor(getResources().getColor(colorId));
    }

    public void setButtonTextColor(int color) {
        this.buttonTextColor = color;
    }

    public void setButtonTextColor(String color) {
        setButtonTextColor(Color.parseColor(color));
    }

    @Override
    public int getLayout() {
        return R.layout.video_card;
    }
}
