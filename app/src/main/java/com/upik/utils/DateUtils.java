package com.upik.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
    public static final long YEAR_IN_MILLIS = DAY_IN_MILLIS * 365;

    private static long MILLIS_VALUE = 1000;
    private static String STRING_TODAY = "Today";
    private static String STRING_YESTERDAY = "Yesterday";

    private static final SimpleDateFormat FULL_DATE_FORMAT;
    private static final SimpleDateFormat FULL_DATETIME_FORMAT;
    private static final SimpleDateFormat SIMPLE_TIME_FORMAT;
    private static final SimpleDateFormat DAY_FORMAT;
    private static final SimpleDateFormat DAY_get;
    private static final SimpleDateFormat DAYNAME_get;
    private static final SimpleDateFormat DAY_getdate;
    private static final SimpleDateFormat DAY_mydate;
    private static final SimpleDateFormat USA_FORMAT;
    private static final SimpleDateFormat TIME_HHMMSS;
    private static final SimpleDateFormat TIME_AMPM;
    private static final SimpleDateFormat DayFormat;
    private static final SimpleDateFormat DateTime;
    private static final SimpleDateFormat NewDateTime;
    private static final SimpleDateFormat fullDate;


    static {
        FULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DayFormat = new SimpleDateFormat("EEE, MMM dd", Locale.ENGLISH);
        SIMPLE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        TIME_HHMMSS = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        TIME_AMPM = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        FULL_DATETIME_FORMAT = new SimpleDateFormat("EEEE d MMM , yyyy", Locale.getDefault());
        DAY_FORMAT = new SimpleDateFormat("MMM, dd", Locale.getDefault());
        DAY_get = new SimpleDateFormat("dd", Locale.getDefault());
        DAYNAME_get = new SimpleDateFormat("EEEE", Locale.getDefault());
        DAY_getdate = new SimpleDateFormat("MMM, yyyy", Locale.getDefault());
        DAY_mydate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        USA_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DateTime = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH);
        NewDateTime = new SimpleDateFormat("MMMM dd,yyyy", Locale.getDefault());
        fullDate = new SimpleDateFormat("EEEE,MMMM dd,yyyy", Locale.getDefault());

    }


    public static String getNewFormat(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = SIMPLE_TIME_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = DayFormat.format(date);
        // Log.e("### newFormat", "" + newFormat);
        return newFormat;
    }

    public static String DateTime(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = SIMPLE_TIME_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = DateTime.format(date);
        return newFormat;
    }

    public static String NewDateTime(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = SIMPLE_TIME_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = NewDateTime.format(date);
        // Log.e("### newFormat", "" + newFormat);
        return newFormat;
    }

    public static String getUSAdate(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = FULL_DATE_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = USA_FORMAT.format(date);
        Log.e("newFormat", "" + newFormat);
        return newFormat;
    }

    public static String getRecurringDate(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = SIMPLE_TIME_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = fullDate.format(date);
        return newFormat;
    }


    public static String getRecurringDayname(String dateTime) {
        String newFormat;
        Date date = null;
        try {
            date = fullDate.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = SIMPLE_TIME_FORMAT.format(date);
        Log.e("sdate here", "" + newFormat);
        return newFormat;
    }

    public static String getmonthandDate(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = FULL_DATE_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = DAY_FORMAT.format(date);
        return newFormat;
    }


    public static String getmonthDate(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = FULL_DATE_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = DAY_getdate.format(date);
        return newFormat;
    }

    public static String getTimeampm(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = TIME_HHMMSS.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = TIME_AMPM.format(date);
        return newFormat;
    }


    public static String getTime(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = FULL_DATE_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = SIMPLE_TIME_FORMAT.format(date);
        return newFormat;
    }

    public static String getDay(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = FULL_DATE_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = DAYNAME_get.format(date);
        return newFormat;
    }

    public static String changeDateTimeFormat(String dateTime) {
        String newFormat = null;
        Date date = null;
        try {
            date = FULL_DATE_FORMAT.parse(dateTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        newFormat = FULL_DATETIME_FORMAT.format(date);
        return newFormat;
    }

    public static String todayDate() {
        String today = null;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        today = dateFormat.format(date);

        return today;
    }


    public static String getChatTime(String string_date) {

        long milliseconds = 0;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date d = f.parse(string_date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return longToMessageListHeaderDate(milliseconds) + " at " + longToMessageDate(milliseconds);
    }

    public static String longToMessageListHeaderDate(long dateLong) {
        String timeString;

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Log.d("Time zone: ", tz.getDisplayName());

        Locale locale = new Locale("en");

        Calendar calendar = Calendar.getInstance();
        int currentDate = calendar.getTime().getDate();

        calendar.setTimeInMillis(dateLong);
        int inputDate = calendar.getTime().getDate();

        if (inputDate == currentDate) {
            timeString = STRING_TODAY;
        } else if (inputDate == currentDate - 1) {
            timeString = STRING_YESTERDAY;
        } else {
            Date time = calendar.getTime();
            timeString = new SimpleDateFormat("EEEE", locale).format(time) + ", " + inputDate + " " +
                    new SimpleDateFormat("MMMM", locale).format(time);
        }

        return timeString;
    }

    public static String longToMessageDate(long dateLong) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateLong);
        String timeString = new SimpleDateFormat("hh:mm a").format(calendar.getTime());
        return timeString;
    }

}