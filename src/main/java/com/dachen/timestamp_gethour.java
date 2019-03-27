package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class timestamp_gethour extends UDF {

    public String evaluate(String time) throws ParseException {
        String output="";
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (time == null || time.length()<10) {
            return output;
        } else if (time.contains("-")) {
            time = String.valueOf(df.parse(time).getTime());
        }
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


    public static void main(String[] args) throws ParseException {
        String time = "2018-11-11 01:01:01";
        timestamp_gethour pt = new timestamp_gethour();
        System.out.println(pt.evaluate(time));
    }
}
