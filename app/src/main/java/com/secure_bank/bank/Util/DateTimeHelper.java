package com.secure_bank.bank.Util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeHelper {
    public static final String FORMAT_DISPLAY = "dd. MM. yyyy HH:mm:ss";
    public static final String FORMAT_WEBAPI_DATE = "dd.MM.yyyy HH:mm";
    public static final String FORMAT_WEBAPI_DATE_FULL = "dd.MM.yyyy HH:mm:ss";
    public static final String FORMAT_WEBAPI_CREATE_SPECIAL = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static Date convertToDateFromString(String string, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date minDate = getMinDate();
        try {
            return simpleDateFormat.parse(string);
        } catch (Exception ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Crash @ converting on ");
            stringBuilder.append(string);
            stringBuilder.append(" with string format: ");
            stringBuilder.append(format);
            Log.e("ao__", stringBuilder.toString());
            ex.printStackTrace();
            return minDate;
        }

    }

    private static Date getMinDate() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.set(Calendar.MONTH, 0);
        gregorianCalendar.set(Calendar.YEAR, 1990);
        return gregorianCalendar.getTime();
    }

    static public String convertDateToFormatString(Calendar date, String outputFormat) {
        if (date == null) {
            date = Calendar.getInstance();
            date.setTime(date.getTime());
        }


        SimpleDateFormat outFormat = new SimpleDateFormat(outputFormat);
        return outFormat.format(date.getTime());
    }

    static public String convertDateToFormatString(Date date, String outputFormat) {
        if (date == null)
            date = getMinDate();

        SimpleDateFormat outFormat = new SimpleDateFormat(outputFormat);
        return outFormat.format(date);
    }

    static public String convertStringToNewFormatString(String string, String inputFormat, String outputFormat) {
        return convertDateToFormatString(convertToDateFromString(string, inputFormat), outputFormat);
    }
}
