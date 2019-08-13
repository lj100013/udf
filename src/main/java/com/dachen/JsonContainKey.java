package com.dachen;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.Set;

public class JsonContainKey extends UDF {
    public boolean evaluate(String parentjson, String sonjson) {
        String regex = "\\s*|\\s*\\{\\s*\\}\\s*";
        //son为空
        if (StringUtils.isEmpty(sonjson) || sonjson.matches(regex)) {
            return true;
        }
        //parent未空
        if (StringUtils.isEmpty(parentjson) || parentjson.matches(regex)) {
            return false;
        }

        JSONObject parent = JSONObject.parseObject(parentjson);
        JSONObject son = JSONObject.parseObject(sonjson);
        Set<String> parentSet = parent.keySet();
        Set<String> sonSet = son.keySet();
        return parentSet.containsAll(sonSet);
    }

    public static void main(String[] args) {
        String parentjson = "{\"_name\":\"value\"}";
        String sonjson = "{\"_nam2e\":\"value\"}";
        System.out.println(new JsonContainKey().evaluate(parentjson, sonjson));
    }
}
