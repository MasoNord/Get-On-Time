package org.masonord.delivery.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date today() {
        return new Date();
    }

    public static String todayToStr() {
        return dateFormat.format(today());
    }

    public static String formattedDate(Date date) {
        return date != null ? dateFormat.format(date) : todayToStr();
    }
}
