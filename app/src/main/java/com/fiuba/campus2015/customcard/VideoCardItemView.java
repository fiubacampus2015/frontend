package com.fiuba.campus2015.customcard;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.cards.internal.BaseButtonsCardItemView;
import com.dexafree.materialList.cards.internal.BaseTextCardItemView;
import com.fiuba.campus2015.R;

public class VideoCardItemView extends BaseTextCardItemView<VideoCard> {


    public VideoCardItemView(Context context) {
        super(context);
    }

    public VideoCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(final VideoCard card) {
        super.build(card);

        // Divider
        View divider = findViewById(R.id.cardDivider);
        divider.setBackgroundColor(card.getDividerColor());

        // DEscription}
     //   TextView description = (TextView) findViewById(R.id.descriptionTextView);
     //   description.setTextColor(#878787); Color.parseColor("#608DFA")

        // Button
        final TextView button2 = (TextView) findViewById(R.id.deleteButtonVideoCard);
        button2.setText(card.getButtonText());
        button2.setTextColor(card.getButtonTextColor());
        Drawable drawable = button2.getCompoundDrawables()[0];
        drawable.setColorFilter(card.getButtonTextColor(), PorterDuff.Mode.SRC_IN);
        button2.setCompoundDrawablesWithIntrinsicBounds(resize(drawable, 50, 50), null, null, null);

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.getOnButtonPressedListener() != null) {
                    card.getOnButtonPressedListener().onButtonPressedListener(button2, card);
                }
            }
        });

        final ImageView imageView = (ImageView)findViewById(R.id.imagePreview3);
        imageView.setImageDrawable(card.getPreview());
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(card.getOnButtonPressedListenerPreview() != null) {
                    card.getOnButtonPressedListenerPreview().onButtonPressedListener(null,null);
                }
            }
        });
    }

    private Drawable resize(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResize = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(getResources(), bitmapResize);
    }

}