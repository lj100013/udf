package com.to8to;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class timestamp_getmonth extends UDF {

    public String evaluate(String time) {
        String output = "";
        SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");
        if (time == null || time.length()<10) {
            return output;
        } else if(time.contains("-")) {
            try {
                time = String.valueOf(month.parse(time).getTime());
            } catch (Exception e) {
                return output;
            }
        } else if (time == null || time.length()<10) {
            return output;
        }
        output = month.format(time.length() == 10 ? new Date(Long.parseLong(time)*1000): new Date(Long.parseLong(time))) ;
        return output;
    }

    public String evaluate(Long time)  {
        String output = "";
        if (time == null || time.toString().length()<10) return output;
        SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");
        String timestamp = time.toString();
        output = month.format(timestamp.length() == 10 ? new Date(Long.parseLong(timestamp)*1000): new Date(Long.parseLong(timestamp))) ;
        return output;
    }

    public String evaluate(String time,Integer months) {
        String result = "";
        if(StringUtils.isBlank(time)) return result;
        try{
            String pat = "yyyy-MM-dd";
            SimpleDateFormat format = new SimpleDateFormat(pat);
            Date date =  format.parse(time+"-01");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH,months);
            result = format.format(calendar.getTime());
            result = result.substring(0,result.lastIndexOf("-"));
        }catch(Exception e){

        }
        return result;
    }


    public static void main(String[] args) {
        try {
            timestamp_getmonth pt = new timestamp_getmonth();
            for(int i = 2300;i<=3000;i++){
                Thread.sleep(300);
                String pat = "yyyy-MM-dd";
                SimpleDateFormat format = new SimpleDateFormat(pat);
                Date date =  format.parse(i+"-01-01");
                System.out.println(i+"-01-01"+"====="+pt.evaluate(date.getTime())+"=="+pt.evaluate(date.getTime()+""));
            }
        }catch (Exception e){

        }

    }
}
