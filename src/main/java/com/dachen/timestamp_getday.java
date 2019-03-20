package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class timestamp_getday extends UDF {

    public String evaluate(String time) {
        String output="";
        if (time.contains("-")) {
            if (time.length() == 8 && Long.parseLong(time) >= 19700101 && Long.parseLong(time) <= 21000101) {
                output = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8);
                return output;
            } else {
                return time;
            }
        }
        if (time == null || time.length()<10) return output;
        SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        output = year.format(date);

        return output;
    }

    public String evaluate(Long time) {
        String output="";
        if (time.toString().length() ==8 && time>=19700101 && time<=21000101){
            output= time.toString().substring(0,4) + "-" + time.toString().substring(4,6) + "-" + time.toString().substring(6,8);
            return output;
        }
        if (time == null || time.toString().length()<10) return output;
        SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = time.toString();
        Timestamp date = new Timestamp(Long.parseLong(timestamp.substring(0,10))*1000);
        output = year.format(date);

        return output;
    }

    public static void main(String[] args) {
        String time = "2018-11-27";
        timestamp_getday pt = new timestamp_getday();
        System.out.println(pt.evaluate(time));
    }
}
