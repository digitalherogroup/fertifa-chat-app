package com.ithd.chat.chatapp.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DateConverter {

    public String formatter(Date date) {
        return convertingDate(date);
    }

    private String convertingDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(date);
    }

    public static String convertDateWithRegex(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.format(new Date(convertStringToTimestamp(date).getTime()));
        } catch (Exception e) {
            System.out.println("Can Not convert time stamp in Date Converter class");
            return "";
        }
    }

    public static String convertDateWithRegex(Timestamp date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.format(new Date(date.getTime()));
        } catch (Exception e) {
            System.out.println("Can Not convert time stamp in Date Converter class");
            return date.toString();
        }
    }

    public static Timestamp convertStringToTimestamp(String dateString) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = null;
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(dateString);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}
