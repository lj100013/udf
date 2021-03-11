package com.to8to;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.ParseException;

public class MonthBegin extends UDF {
    public int evaluate(int data) {
        String time = Integer.toString(data);
        int result = Integer.parseInt(String.valueOf(time.substring(0, 6)) + "01");
        return result;
    }

    public int evaluate(String data) {
        int result = Integer.parseInt(String.valueOf(data.substring(0, 6)) + "01");
        return result;
    }

    public static void main(String[] args) throws ParseException {
        MonthBegin monthBegin = new MonthBegin();
        System.out.println(monthBegin.evaluate("20200122"));
        System.out.println(monthBegin.evaluate(20200122));
    }
}