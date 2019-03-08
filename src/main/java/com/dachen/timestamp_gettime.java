package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class timestamp_gettime extends UDF {

    public String evaluate(String time){
        String output="";
        if (time == null || time.length()<10) return output;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        output = sdf.format(date);

        return output;
    }

    public String evaluate(Long time){
        String output="";
        if (time == null || time.toString().length()<10) return output;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = time.toString();
        Timestamp date = new Timestamp(Long.parseLong(timestamp.substring(0,10))*1000);
        output = sdf.format(date);

        return output;
    }

    public static void main(String[] args){
        String time = "1502791989015";
        timestamp_gettime pt = new timestamp_gettime();
        System.out.println(pt.evaluate(time));
    }
}
