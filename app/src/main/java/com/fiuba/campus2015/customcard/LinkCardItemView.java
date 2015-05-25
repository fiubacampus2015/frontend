package com.fiuba.campus2015.customcard;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.cards.internal.BaseTextCardItemView;
import com.dexafree.materialList.model.CardItemView;
import com.fiuba.campus2015.R;

public class LinkCardItemView extends CardItemView<LinkCard> {

    TextView mTitle;
    TextView mDescription;

    public LinkCardItemView(Context context) {
        super(context);
    }

    public LinkCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void build(final LinkCard card) {
        setTitle(card.getTitle());
        setDescription(card.getDescription());

        // Divider
        View divider = findViewById(R.id.cardDivider);
        divider.setBackgroundColor(card.getDividerColor());

        // Button
        final TextView button = (TextView) findViewById(R.id.deleteButtonLinkCard);
        button.setText(card.getButtonText());
        button.setTextColor(card.getButtonTextColor());
        Drawable drawable = button.getCompoundDrawables()[0];
        drawable.setColorFilter(card.getButtonTextColor(), PorterDuff.Mode.SRC_IN);
        button.setCompoundDrawablesWithIntrinsicBounds(resize(drawable, 50, 50), null, null, null);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.getOnButtonPressedListener() != null) {
                    card.getOnButtonPressedListener().onButtonPressedListener(button, card);
                }
            }
        });
    }

    private Drawable resize(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResize = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(getResources(), bitmapResize);
    }

    public void setTitle(String title){
        mTitle = (TextView)findViewById(R.id.titleTextView);
        mTitle.setText(title);
    }

    public void setDescription(String description){
        mDescription = (TextView)findViewById(R.id.descriptionTextView);
        mDescription.setClickable(true);
        mDescription.setMovementMethod(LinkMovementMethod.getInstance());
        mDescription.setText(Html.fromHtml(description));
    }

}