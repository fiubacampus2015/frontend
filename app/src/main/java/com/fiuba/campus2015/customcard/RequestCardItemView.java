package com.fiuba.campus2015.customcard;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.cards.internal.BaseTextCardItemView;
import com.fiuba.campus2015.R;
import com.fiuba.campus2015.extras.ButtonFloatMaterial;

public class RequestCardItemView extends BaseTextCardItemView<RequestCard> {


    public RequestCardItemView(Context context) {
        super(context);
    }

    public RequestCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RequestCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(final RequestCard card) {
        super.build(card);


        final ButtonFloatMaterial accept = (ButtonFloatMaterial)findViewById(R.id.buttonAcceptRequestGroup);
        accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(card.getOnButtonPressedListenerAccept() != null) {
                    card.getOnButtonPressedListenerAccept().onButtonPressedListener(null,null);
                }
            }
        });

        final ButtonFloatMaterial reject = (ButtonFloatMaterial)findViewById(R.id.buttonRejectRequestGroup);
        reject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(card.getOnButtonPressedListenerReject() != null) {
                    card.getOnButtonPressedListenerReject().onButtonPressedListener(null,null);
                }
            }
        });
    }

}