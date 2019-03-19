package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class timestamp_getweek extends UDF {

    public String evaluate(String time) throws ParseException {
        String output="";
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (time == null || time.length()<10) return output;
        if (time.contains("-")) time = String.valueOf(df.parse(time).getTime());
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        String week = "";
        if (df.format(date).equals("2018-12-31")) {
            output = "2019" + "-" + week.format("%02d",calendar.get(Calendar.WEEK_OF_YEAR));
        } else {
            output = year.format(date) + "-" + week.format("%02d", calendar.get(Calendar.WEEK_OF_YEAR));
        }
        return output;
    }

    public String evaluate(Long time) {
        String output="";
        if (time == null || time.toString().length()<10) return output;
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = time.toString();
        Timestamp date = new Timestamp(Long.parseLong(timestamp.substring(0,10))*1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        String week = "";
        if (df.format(date).equals("2018-12-31")) {
            output = "2019" + "-" + week.format("%02d",calendar.get(Calendar.WEEK_OF_YEAR));
        } else {
            output = year.format(date) + "-" + week.format("%02d",calendar.get(Calendar.WEEK_OF_YEAR));
        }
        return output;
    }


    public static void main(String[] args) throws ParseException {
        Long time = 1546185600L;
        timestamp_getweek pt = new timestamp_getweek();
        System.out.println(pt.evaluate(time));
    }

}


