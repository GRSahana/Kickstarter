package com.sahanaprojects.kickstarter.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter {

    //Convert ZonedDate to datetime String
    public static String convertDateTimetoDate(String dateTime){
        String DATE_FORMAT_I = "yyyy-MM-dd'T'HH:mm:ss";
        String DATE_FORMAT_O = "yyyy-MM-dd";


        SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
        SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);

        Date date = null;
        try {
            date = formatInput.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateString = formatOutput.format(date);
        return dateString;

    }

    //Convert ZonedDate to Date
    public static Date convertZoneDatetoDate(String dateTime){
        String DATE_FORMAT_I = "yyyy-MM-dd'T'HH:mm:ss";
        String DATE_FORMAT_O = "yyyy-MM-dd HH:mm:ss";


        SimpleDateFormat formatInput = new SimpleDateFormat(DATE_FORMAT_I);
        SimpleDateFormat formatOutput = new SimpleDateFormat(DATE_FORMAT_O);

        Date date = null;
        try {
            date = formatInput.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }
}
