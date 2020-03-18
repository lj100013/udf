package com.dachen;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;

public class timestamp_get0tmsp extends UDF {

    public String evaluate(String time){
        String result = "";
        if(StringUtils.isBlank(time)) return result;
        try{
            String pat = "yyyy-MM-dd";
            SimpleDateFormat format = new SimpleDateFormat(pat);
            Date date =  format.parse(time);
            result = date.getTime()+"";
        }catch(Exception e){
        }
        return result;
    }

    public static void main(String[] args){
        String time = "2015-01-01";
        timestamp_get0tmsp pt = new timestamp_get0tmsp();
        System.out.println(pt.evaluate(time));
    }
}
