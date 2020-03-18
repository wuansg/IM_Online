package xyz.silverspoon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {

    public static Date transform(Date date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd mm:ss");
        return simpleDateFormat.parse(date.toString());
    }
}
