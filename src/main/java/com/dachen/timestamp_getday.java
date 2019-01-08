package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class timestamp_getday extends UDF {

    public String evaluate(String time) {
        String output="";
        SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        output = year.format(date);

        return output;
    }

    public String evaluate(Long time) {
        String output="";
        SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = time.toString();
        Timestamp date = new Timestamp(Long.parseLong(timestamp.substring(0,10))*1000);
        output = year.format(date);

        return output;
    }

    public static void main(String[] args) {
        String time = "1502791989015";
        timestamp_getday pt = new timestamp_getday();
        System.out.println(pt.evaluate(time));
    }
}