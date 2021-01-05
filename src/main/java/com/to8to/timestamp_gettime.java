package com.to8to;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;

public class timestamp_gettime extends UDF {

    public String evaluate(String time){
        String output="";
        if (time == null || time.length()<10) return output;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        output = sdf.format(time.length() == 10 ? new Date(Long.parseLong(time)*1000) : new Date(Long.parseLong(time)));
        return output;
    }

    public String evaluate(Long time){
        String output="";
        if (time == null || time.toString().length()<10) return output;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = time.toString();
        output = sdf.format(timestamp.length() == 10 ?  new Date(Long.parseLong(timestamp)*1000) : new Date(Long.parseLong(timestamp)));
        return output;
    }

    public static void main(String[] args){
        try {
            timestamp_gettime pt = new timestamp_gettime();
            for(int i = 2300;i<=3000;i++){
                Thread.sleep(300);
                String pat = "yyyy-MM-dd";
                SimpleDateFormat format = new SimpleDateFormat(pat);
                Date date =  format.parse(i+"-01-01");
                System.out.println(i+"-01-01"+"====="+pt.evaluate(date.getTime())+"=="+pt.evaluate(date.getTime()+""));
            }
        }catch(Exception e){

        }
    }
}
