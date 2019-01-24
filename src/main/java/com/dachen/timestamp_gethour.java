package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class timestamp_gethour extends UDF {

    public String evaluate(String time) {
        String output="";
        if (time == null || time.length()<10) return output;
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        output = hour.format(date);
        return output;
    }

    public String evaluate(Long time) {
        String output="";
        if (time == null || time.toString().length()<10) return output;
        String timestamp = time.toString();
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        Timestamp date = new Timestamp(Long.parseLong(timestamp.substring(0,10))*1000);
        output = hour.format(date);
        return output;
    }


    public static void main(String[] args) {
        long time = Long.parseLong("1502791989015");
        timestamp_gethour pt = new timestamp_gethour();
        System.out.println(pt.evaluate(time));
    }
}
