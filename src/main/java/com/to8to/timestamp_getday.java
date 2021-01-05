package com.to8to;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;

public class timestamp_getday extends UDF {

    public String evaluate(String time) {
        String output = "";
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        if (time == null || time.length() < 10) {
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
        output = day.format(time.length() == 10 ? new Date(Long.parseLong(time) * 1000) : new Date(Long.parseLong(time)));
        return output;
    }

    public String evaluate(Long time) {
        String output = "";
        if (time == null || time.toString().length() < 10) {
            return output;
        } else if (time.toString().length() == 8 && time >= 19700101 && time <= 21000101) {
            output = time.toString().substring(0, 4) + "-" + time.toString().substring(4, 6) + "-" + time.toString().substring(6, 8);
            return output;
        }
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        String timestamp = time.toString();
        output = day.format(timestamp.length() == 10 ? new Date(Long.parseLong(timestamp) * 1000) : new Date(Long.parseLong(timestamp)));
        return output;
    }

    public String evaluate(String time, String standard) {
        String output = "";
        SimpleDateFormat day = new SimpleDateFormat(standard);
        if (time == null || time.length() < 10) {
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
        output = day.format(time.length() == 10 ? new Date(Long.parseLong(time) * 1000) : new Date(Long.parseLong(time)));
        return output;
    }

    public String evaluate(Long time, String standard) {
        String output = "";
        if (time == null || time.toString().length() < 10) {
            return output;
        } else if (time.toString().length() == 8 && time >= 19700101 && time <= 21000101) {
            output = time.toString().substring(0, 4) + "-" + time.toString().substring(4, 6) + "-" + time.toString().substring(6, 8);
            return output;
        }
        SimpleDateFormat day = new SimpleDateFormat(standard);
        String timestamp = time.toString();
        output = day.format(timestamp.length() == 10 ? new Date(Long.parseLong(timestamp) * 1000) : new Date(Long.parseLong(timestamp)));
        return output;
    }

    public static void main(String[] args) {
        try {
            timestamp_getday pt = new timestamp_getday();
            for (int i = 1970; i <= 3000; i++) {
                Thread.sleep(300);
                String pat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat format = new SimpleDateFormat(pat);
                Date date = format.parse(i + "-12-01 01:01:01");
                System.out.println(i + "-01-01" + "=====" + pt.evaluate(date.getTime()) + "==" + pt.evaluate(date.getTime() + ""));
            }
        } catch (Exception e) {

        }
    }
}
