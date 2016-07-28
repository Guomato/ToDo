package com.guoyonghui.todo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateHelper {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);

    public static String format(Date date) {
        return FORMAT.format(date);
    }

    public static Date parse(String date) {
        try {
            return FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();

            return null;
        }
    }

}
