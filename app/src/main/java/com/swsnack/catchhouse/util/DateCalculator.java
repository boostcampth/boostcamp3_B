package com.swsnack.catchhouse.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateCalculator {
    private static final Calendar currentDate = new GregorianCalendar(Locale.KOREA);

    public static int getYear() {
        return currentDate.get(Calendar.YEAR);
    }

    public static int getMonth() {
        return currentDate.get(Calendar.MONTH);
    }

    public static int getDay() {
        return currentDate.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDiffDate(Date beginDate, Date endDate) {
        int diffDay;
        long diff;

        diff = endDate.getTime() - beginDate.getTime();
        diffDay = (int) (diff / (24 * 60 * 60 * 1000));

        return diffDay > 0 ? diffDay : 0;
    }

    public static String createDateString(int year, int month ,int day) {
        return year + "-" +
                DateCalculator.refineDate(month) + "-" +
                DateCalculator.refineDate(day);
    }

    public static String refineDate(int num) {
        if(num < 10) {
            return "0" + num;
        } else {
            return "" + num;
        }
    }

}
