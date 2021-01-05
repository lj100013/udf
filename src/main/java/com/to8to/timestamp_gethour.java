package com.to8to;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;

public class timestamp_gethour extends UDF {

    public String evaluate(String time) {
        String output="";
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (time == null || time.length()<10) {
            return output;
        } else if (time.contains("-")) {
            try {
                time = String.valueOf(df.parse(time).getTime());
            } catch (Exception e) {
                return output;
            }
        }
        output = hour.format(time.length() == 10 ? new Date(Long.parseLong(time)*1000): new Date(Long.parseLong(time)));
        return output;
    }

    public String evaluate(Long time) {
        String output="";
        if (time == null || time.toString().length()<10) return output;
        String timestamp = time.toString();
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        output = hour.format(timestamp.length() == 10 ? new Date(Long.parseLong(timestamp)*1000): new Date(Long.parseLong(timestamp)));
        return output;
    }


    public static void main(String[] args) {
        try {
            timestamp_gethour pt = new timestamp_gethour();
            for(int i = 1970;i<=3000;i++){
                Thread.sleep(300);
                String pat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat format = new SimpleDateFormat(pat);
                Date date =  format.parse(i+"-01-01 01:01:01");
                System.out.println(i+"-01-01"+"====="+pt.evaluate(date.getTime())+"=="+pt.evaluate(date.getTime()+""));
            }
        }catch (Exception e){

        }
    }
}
