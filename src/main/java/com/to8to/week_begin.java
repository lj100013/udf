package com.to8to;

import org.apache.hadoop.hive.ql.exec.UDF;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class week_begin extends UDF {

    public String evaluate(String time) {
        String output="";
        Date date = null;
        SimpleDateFormat day_fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        if (time == null || time.length()<8) {
            return output;
        } else if (time.contains("-")) {
            try {
                String time_stamp_str = String.valueOf(day_fmt.parse(time).getTime());
                String d = day.format(new Date(Long.parseLong(time_stamp_str))); // 时间戳转换日期
                date = day.parse(d);
            } catch (Exception e) {
                return output;
            }
        } else if (time.length() == 8 && Long.parseLong(time) >= 19700101 && Long.parseLong(time) <= 21000101) {
            try {
                date = day.parse(time);
            } catch (Exception e) {
                return output;
            }
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int i = calendar.get(Calendar.DAY_OF_WEEK);  // 一周的第几天
        calendar.add(Calendar.DATE,-(i-1));
        output = day.format(calendar.getTime());
        return output;
    }

    public String evaluate(Long time) {
        String output="";
        Date date = null;
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        if (time == null){
            return output;
        } else if (time.toString().length() == 8 && time>=19700101 && time<=21000101){
            try {
                date = new Date(day.parse(time.toString()).getTime());
            } catch (Exception e) {
                return output;
            }
        } else if (time.toString().length()<10) {
            return output;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int i = calendar.get(Calendar.DAY_OF_WEEK);  // 一周的第几天
        calendar.add(Calendar.DATE,-(i-1));
        output = day.format(calendar.getTime());
        return output;
    }

    public static void main(String[] args) {
        week_begin mb = new week_begin();
//        String s1 = mb.evaluate(20200917L);
//        String s2 = mb.evaluate("2020-09-17");
//        String s3 = mb.evaluate("20201131");
//        System.out.println(s1+" "+s2+" "+s3);
//        System.out.println(s3);
        System.out.println(mb.evaluate(args[0].toString()));
    }
}
