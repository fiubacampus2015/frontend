package com.fiuba.campus2015.extras;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

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

    public static String getBirthdayFormatted(String birthday) {
        String formatted = "";
        if(birthday != null && !birthday.isEmpty()) {
            int month_ = Integer.parseInt(birthday.substring(5,7));
            formatted = birthday.substring(8,10) + " de ";

            switch (month_) {
                case(1): return formatted + "Enero de " + birthday.substring(0,4);
                case(2): return formatted + "Febrero de " + birthday.substring(0,4);
                case(3): return formatted + "Marzo de " + birthday.substring(0,4);
                case(4): return formatted + "Abril de " + birthday.substring(0,4);
                case(5): return formatted + "Mayo de " + birthday.substring(0,4);
                case(6): return formatted + "Junio de " + birthday.substring(0,4);
                case(7): return formatted + "Julio de " + birthday.substring(0,4);
                case(8): return formatted + "Agosto de " + birthday.substring(0,4);
                case(9): return formatted + "Septiembre de " + birthday.substring(0,4);
                case(10): return formatted + "Octubre de " + birthday.substring(0,4);
                case(11): return formatted + "Noviembre de " + birthday.substring(0,4);
                default: return formatted + "Diciembre de " + birthday.substring(0,4);
            }
        }
        return formatted;
    }

    public static Bitmap getPhoto(String path) {
        byte[] decodedString = Base64.decode(path , Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        //options.inSampleSize = 8;
        Bitmap photoBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,options);
        return Bitmap.createScaledBitmap(photoBitmap, 200, 200, true);
    }

    public static String getGender(String mgender) {
        if(mgender.equals("M")) {
            return "Masculino";
        } else {
            return "Femenino";
        }
    }
}
