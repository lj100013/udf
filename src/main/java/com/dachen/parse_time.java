package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class parse_time extends UDF {

    public String evaluate(String time,String args) {
        String output="";
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        if ("year".equals(args)) {
            output = year.format(date);
        } else if ("month".equals(args)) {
            output = month.format(date);
        } else if ("week".equals(args)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(date);
            output = year.format(date) + "-" + calendar.get(Calendar.WEEK_OF_YEAR);
        } else if ("hour".equals(args)) {
            output = hour.format(date);
        }
        return output;
    }

    public static void main(String[] args) {
        String time = "1502791989015";
        parse_time pt = new parse_time();
        System.out.println(pt.evaluate(time,"hour"));
    }
}


