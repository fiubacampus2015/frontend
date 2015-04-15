package com.fiuba.campus2015.extras;

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

}
