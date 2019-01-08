package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class timestamp_getmonth extends UDF {

    public String evaluate(String time)  {
        String output = "";
        SimpleDateFormat month = new SimpleDateFormat("yyyy-MM");
        Timestamp date = new Timestamp(Long.parseLong(time.substring(0, 10)) * 1000);
        output = month.format(date);
        return output;
    }

    public static void main(String[] args) {
        String time = "1502791989015";
        timestamp_getmonth pt = new timestamp_getmonth();
        System.out.println(pt.evaluate(time));
    }
}
