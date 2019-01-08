package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.Calendar;

public class idcard_analysis extends UDF {

    public String evaluate(String idnumber,String args) {
        String output ="";
        Calendar cal = Calendar.getInstance();
        try {
            if (idnumber.length()!=18|!(idnumber instanceof String)) return output;
        } catch (Exception e){
            return output;
        }
        if ("birthday".equals(args)) {
            output = idnumber.substring(6,10) + "-" + idnumber.substring(10,12) + "-" + idnumber.substring(12,14);
        } else if ("age".equals(args)) {
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

            int yearBirth = Integer.parseInt(idnumber.substring(6,10));
            int monthBirth = Integer.parseInt(idnumber.substring(10,12));
            int dayOfMonthBirth = Integer.parseInt(idnumber.substring(12,14));

            int age = yearNow - yearBirth;

            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) age--;
                }else{
                    age--;
                }
            }

            output = String.valueOf(age);
        } else if ("sex".equals(args)) {
            String male2 = "13579";
            if (male2.indexOf(idnumber.charAt(16))>=0) {
                output = "男";
            } else {
                output = "女";
            }
        }
        return output;
    }


    public static void main(String[] args) {
        String id = "430406199607162518";
        idcard_analysis ia = new idcard_analysis();
        System.out.println(ia.evaluate(id,"age"));
    }
}
