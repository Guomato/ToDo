package com.guoyonghui.todo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class CalendarFormatHelper {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);

    public static String format(Calendar alarm) {
        return FORMAT.format(alarm.getTime());
    }

    public static Calendar parse(Date alarm) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(alarm);
        return calendar;
    }

    public static Calendar parse(String alarm) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(FORMAT.parse(alarm));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAlarmLegal(String alarm) {
        if (alarm == null) {
            return false;
        }
        try {
            Date date = FORMAT.parse(alarm);
            return date.after(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

}
