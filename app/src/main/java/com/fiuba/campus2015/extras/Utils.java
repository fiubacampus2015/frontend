package com.fiuba.campus2015.extras;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static com.fiuba.campus2015.extras.Constants.*;

/**
 * Created by gonzalovelasco on 14/4/15.
 */
public class Utils {

    public static Calendar stringToCalendar(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
        Date date = sdf.parse(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static String getMonth(int month) {
        switch (month) {
            case(1): return "Enero";
            case(2): return "Febrero";
            case(3): return "Marzo";
            case(4): return "Abril";
            case(5): return "Mayo";
            case(6): return "Junio";
            case(7): return "Julio";
            case(8): return "Agosto";
            case(9): return "Septiembre";
            case(10): return "Octubre";
            case(11): return "Noviembre";
            default: return "Diciembre";
        }
    }

}
