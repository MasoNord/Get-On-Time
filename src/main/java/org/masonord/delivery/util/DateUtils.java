package org.masonord.delivery.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public static Boolean DetermineDateBetweenTwoDates(String startDate, String endDate, String completedOrderDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        Date completed = sdf.parse(completedOrderDate);

        int result = completed.compareTo(start);
        if (result > 0) {
           result = completed.compareTo(end);

            return result < 0;
        }

        return false;
    }

    public static long DifferenceBetweenTwoDates(String date1, String date2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date firstDate = sdf.parse(date1);
        Date secondDate = sdf.parse(date2);

        long diffInMilliseconds = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);

        return diff * 24;
    }
}
