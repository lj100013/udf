package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class timestamp_getweek extends UDF {

    public String evaluate(String time) {
        String output="";
        if (time == null || time.length()<10) return output;
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        output = year.format(date) + "-" + calendar.get(Calendar.WEEK_OF_YEAR);
        return output;
    }

    public String evaluate(Long time) {
        String output="";
        if (time == null || time.toString().length()<10) return output;
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        String timestamp = time.toString();
        Timestamp date = new Timestamp(Long.parseLong(timestamp.substring(0,10))*1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        output = year.format(date) + "-" + calendar.get(Calendar.WEEK_OF_YEAR);
        return output;
    }


    public static void main(String[] args) {
        String time = "1502791989015";
        timestamp_getweek pt = new timestamp_getweek();
        System.out.println(pt.evaluate(time));
    }

}


