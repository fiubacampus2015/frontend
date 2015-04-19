package com.fiuba.campus2015.extras;

import android.content.Context;
import android.util.AttributeSet;

import com.fiuba.campus2015.R;
import com.gc.materialdesign.views.ButtonFloat;

/**
 * Created by gonzalovelasco on 19/4/15.
 */
public class ButtonFloatMaterial extends ButtonFloat {

    public ButtonFloatMaterial(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.back_button_float);

    }
}
