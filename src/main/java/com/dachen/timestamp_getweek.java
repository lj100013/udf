package com.dachen;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class timestamp_getweek extends UDF {

    public String evaluate(String time) {
        String output="";
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (time == null || time.length()<10) {
            return output;
        } else if (time.contains("-")) {
            try {
                time = String.valueOf(df.parse(time).getTime());
            } catch (Exception e) {
                return output;
            }
        } else if (time == null || time.length()<10) {
            return output;
        }
        Date date = null;
        if(time.length() == 10){
            date = new Date(Long.parseLong(time)*1000);
        }else{
            date = new Date(Long.parseLong(time));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        String week = "";
        if (calendar.get(Calendar.MONTH) == 11 && calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
            output =  Integer.parseInt(year.format(date)) +1 + "-" + week.format("%02d",calendar.get(Calendar.WEEK_OF_YEAR));
        } else {
            output = year.format(date) + "-" + week.format("%02d", calendar.get(Calendar.WEEK_OF_YEAR));
        }
        return output;
    }

    public String evaluate(Long time) {
        String output="";
        if (time == null || time.toString().length()<10) return output;
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String timestamp = time.toString();
        Date date = null;
        if(timestamp.length() == 10){
            date = new Date(time*1000);
        }else{
            date = new Date(time);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        String week = "";
        if (calendar.get(Calendar.MONTH) == 11 && calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
            output =  Integer.parseInt(year.format(date)) +1 + "-" + week.format("%02d",calendar.get(Calendar.WEEK_OF_YEAR));
        } else {
            output = year.format(date) + "-" + week.format("%02d", calendar.get(Calendar.WEEK_OF_YEAR));
        }
        return output;
    }

    /**
     * 一年多少周
     * @param time
     * @param weeks
     * @return
     */
    public String evaluate(String time,Integer weeks) {
        String result = "";
        if(StringUtils.isBlank(time)) return result;
        try{
            String pat = "yyyy-MM-dd";
            String year = time.substring(0,time.indexOf("-"));
            String week = time.substring(time.indexOf("-")+1,time.length());
            if(Integer.parseInt(week) > 53) return result;
            SimpleDateFormat format = new SimpleDateFormat(pat);
            Date date =  format.parse(year+"-01-01");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.add(Calendar.WEEK_OF_YEAR,Integer.parseInt(week)-1+weeks);
            if (calendar.get(Calendar.MONTH) == 11 && calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
                result = calendar.get(Calendar.YEAR)+1+"-"+String.format("%02d",calendar.get(Calendar.WEEK_OF_YEAR));
            } else {
                result = calendar.get(Calendar.YEAR)+"-"+String.format("%02d",calendar.get(Calendar.WEEK_OF_YEAR));
            }
        }catch(Exception e){
        }
        return result;
    }



    public static void main(String[] args) {
        try {
            timestamp_getweek pt = new timestamp_getweek();
//            String time = "2020-51";
//            timestamp_getweek pt = new timestamp_getweek();
//            System.out.println(pt.evaluate(time,5));
            for(int i = 2300;i<=3000;i++){
                Thread.sleep(300);
                String pat = "yyyy-MM-dd";
                SimpleDateFormat format = new SimpleDateFormat(pat);
                Date date =  format.parse(i+"-01-01");
                System.out.println(i+"-01-01"+"====="+pt.evaluate(date.getTime())+"=="+pt.evaluate(date.getTime()+"") + "***" + pt.evaluate(i+"-01",5));
            }

        }catch(Exception e){

        }

    }

}


