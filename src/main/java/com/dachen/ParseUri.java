package com.dachen;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

public class ParseUri extends UDF {
    public String evaluate(String uri) {
        if (StringUtils.isBlank(uri)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String[] uris = uri.split("/");
        for (String u : uris) {
            if (StringUtils.isBlank(u)) {
                continue;
            }
            if (u.matches("\\d+")) {
                break;
            }
            if (u.length() > 23 && u.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{23,}$")) {
                System.out.println(u);
            }
            sb.append("/").append(u);
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        String uri = "/";
        System.out.println(new ParseUri().evaluate(uri));
    }
}