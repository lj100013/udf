package com.to8to;

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
            if (u.matches("\\d+") || u.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{24,}$")) {
                break;
            }

            sb.append("/").append(u);
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        String uri = "/circle-school/course/detail/5b59401bc03cbe0f6439d705/5b7438df40ae505c0e0a7513";
        System.out.println(new ParseUri().evaluate(uri));
    }
}