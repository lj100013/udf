package com.to8to;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlDecode extends UDF {
    public String evaluate(String url) {
        String result = "";
        if (null == url) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String evaluate(String url, String param) {
        String result = evaluate(url);
        if (StringUtils.isBlank(result) || StringUtils.isBlank(param)) {
            return "";
        }

        String pattern = param + "\\s*=\\s*.*";
        String regex = param + "\\s*=\\s*";


        String[] results = result.split("&");
        for (String r : results) {
            Pattern pa = Pattern.compile(pattern);
            Matcher m = pa.matcher(r);
            if (m.find()) {
                return m.group().trim().replaceAll(regex, "");
            }
        }
        return "";
    }

    @Test
    public void test() {
        String url = "https://xg.mediportal.com.cn/doctorCircle/web/H5/callApp.html?app=YSQ&path=app%3A%2F%2FclassInfo%3Fid%3D681928369088008192";
        System.out.println(new UrlDecode().evaluate(url));
        System.out.println(new UrlDecode().evaluate(url, "id"));
    }
}
