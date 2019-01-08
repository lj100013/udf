package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class timestamp_gethour extends UDF {

    public String evaluate(String time) {
        String output="";
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        output = hour.format(date);
        return output;
    }

    public static void main(String[] args) {
        String time = "1502791989015";
        timestamp_gethour pt = new timestamp_gethour();
        System.out.println(pt.evaluate(time));
    }
}
