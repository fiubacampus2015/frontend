package com.fiuba.campus2015.customcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.dexafree.materialList.cards.internal.BaseTextCardItemView;
import com.fiuba.campus2015.R;

public class TextCardItemView extends BaseTextCardItemView<TextCard> {

    public TextCardItemView(Context context) {
        super(context);
    }

    public TextCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(final TextCard card) {
        super.build(card);

        // Divider
        View divider = findViewById(R.id.cardDivider);
        divider.setBackgroundColor(card.getDividerColor());

        // Button
        final TextView button = (TextView) findViewById(R.id.deleteButtonTextCard);
        button.setText(card.getButtonText());
        button.setTextColor(card.getButtonTextColor());
        Drawable drawable = button.getCompoundDrawables()[0];
        drawable.setColorFilter(card.getButtonTextColor(), PorterDuff.Mode.SRC_IN);
        button.setCompoundDrawablesWithIntrinsicBounds(resize(drawable, 50, 50), null, null, null);

        if(card.isDeleteable()) {
            button.setVisibility(VISIBLE);
            divider.setVisibility(VISIBLE);
        }
        else {
            button.setVisibility(INVISIBLE);
            divider.setVisibility(INVISIBLE);
        }

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.getOnButtonPressedListener() != null) {
                    card.getOnButtonPressedListener().onButtonPressedListener(button, card);
                }
            }
        });

        if(card.areHidden()) {
            TextView title = (TextView) findViewById(R.id.titleTextView);
            title.setVisibility(GONE);
            TextView delete = (TextView) findViewById(R.id.deleteButtonTextCard);
            delete.setVisibility(GONE);
            divider.setVisibility(GONE);
        }
    }

    private Drawable resize(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResize = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(getResources(), bitmapResize);
    }
}
