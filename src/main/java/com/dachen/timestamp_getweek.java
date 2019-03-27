package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class timestamp_getweek extends UDF {

    public String evaluate(String time) throws ParseException {
        String output="";
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (time == null || time.length()<10) {
            return output;
        } else if (time.contains("-")) {
            time = String.valueOf(df.parse(time).getTime());
        } else if (time == null || time.length()<10) {
            return output;
        }
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        String week = "";
        if (calendar.get(Calendar.MONDAY) == 11 && calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
            output =  Integer.parseInt(year.format(date)) +1 + "-" + week.format("%02d",calendar.get(Calendar.WEEK_OF_YEAR));
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
        if (calendar.get(Calendar.MONDAY) == 11 && calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
            output =  Integer.parseInt(year.format(date)) +1 + "-" + week.format("%02d",calendar.get(Calendar.WEEK_OF_YEAR));
        } else {
            output = year.format(date) + "-" + week.format("%02d", calendar.get(Calendar.WEEK_OF_YEAR));
        }
        return output;
    }


    public static void main(String[] args) throws ParseException {
        for(int i =0;i<10;i++){
        String time = 2001 + i + "-12-31";
        timestamp_getweek pt = new timestamp_getweek();
        System.out.println(time + "  " + pt.evaluate(time));
        }
    }

}


