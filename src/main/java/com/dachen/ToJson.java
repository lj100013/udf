package com.dachen;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ToJson extends UDF {
    public String toJsonString(String... kvs) {
        try {
            //未传参,或不成对
            if (kvs.length < 1 || kvs.length % 2 == 1) {
                return "";
            }

            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < kvs.length; ) {
                map.put(kvs[i].toString(), kvs[i + 1]);
                i += 2;
            }

            return JSON.toJSONString(map);

        } catch (Exception e) {
            return "";
        }
    }

    public String evaluate(String k, String v) {
        return toJsonString(k, v);
    }

    public String evaluate(String k, String v, String k2, String v2) {
        return toJsonString(k, v, k2, v2);
    }

    public String evaluate(String k, String v, String k2, String v2, String k3, String v3) {
        return toJsonString(k, v, k2, v2, k3, v3);
    }

    public String evaluate(String k, String v, String k2, String v2, String k3, String v3, String k4, String v4) {
        return toJsonString(k, v, k2, v2, k3, v3, k4, v4);
    }

    public String evaluate(String k, String v, String k2, String v2, String k3, String v3, String k4, String v4, String k5, String v5) {
        return toJsonString(k, v, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public String evaluate(String k, String v, String k2, String v2, String k3, String v3, String k4, String v4, String k5, String v5, String k6, String v6) {
        return toJsonString(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    public String evaluate(String k, String v, String k2, String v2, String k3, String v3, String k4, String v4, String k5, String v5, String k6, String v6, String k7, String v7) {
        return toJsonString(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    public String evaluate(String k, String v, String k2, String v2, String k3, String v3, String k4, String v4, String k5, String v5, String k6, String v6, String k7, String v7, String k8, String v8) {
        return toJsonString(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    public String evaluate(String k, String v, String k2, String v2, String k3, String v3, String k4, String v4, String k5, String v5, String k6, String v6, String k7, String v7, String k8, String v8, String k9, String v9) {
        return toJsonString(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    public String evaluate(String k, String v, String k2, String v2, String k3, String v3, String k4, String v4, String k5, String v5, String k6, String v6, String k7, String v7, String k8, String v8, String k9, String v9, String k10, String v10) {
        return toJsonString(k, v, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    @Test
    public void test() {
        System.out.println(evaluate("a", "a", "b", "b", "c", "", "d", "", "e", "", "f", "", "g", "", "h", "", "i", "", "j", ""));
    }
}
