package com.dachen;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class timestamp_getday extends UDF {

    public String evaluate(String time) {
        String output="";
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        if (time == null || time.length()<10) {
            return output;
        } else if (time.contains("-")) {
            try {
                time = String.valueOf(day.parse(time).getTime());
            } catch (Exception e) {
                return output;
            }
        } else if (time.length() == 8 && Long.parseLong(time) >= 19700101 && Long.parseLong(time) <= 21000101) {
            output = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8);
            return output;
        }
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0,10))*1000);
        output = day.format(date);

        return output;
    }

    public String evaluate(Long time) {
        String output="";
        if (time.toString().length() ==8 && time>=19700101 && time<=21000101){
            output= time.toString().substring(0,4) + "-" + time.toString().substring(4,6) + "-" + time.toString().substring(6,8);
            return output;
        }  else if (time == null || time.toString().length()<10) {
            return output;
        }
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = time.toString();
        Timestamp date = new Timestamp(Long.parseLong(timestamp.substring(0,10))*1000);
        output = day.format(date);

        return output;
    }

    public String evaluate(String time,Integer days) {
        String result = "";
        if(StringUtils.isBlank(time)) return result;
        try{
            String pat = "yyyy-MM-dd";
            SimpleDateFormat format = new SimpleDateFormat(pat);
            Date date =  format.parse(time);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,days);
            result = format.format(calendar.getTime());
        }catch(Exception e){

        }
        return result;
    }


    public static void main(String[] args) {
        String time = "2019-01-01";
        timestamp_getday pt = new timestamp_getday();
        System.out.println(pt.evaluate(time,31));
    }
}
