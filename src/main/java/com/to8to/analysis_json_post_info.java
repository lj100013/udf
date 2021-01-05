package com.to8to;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luoianun
 * @create 2018-11-14 11:40
 * @desc 解析basepost.post_info集合中cards數組的第一個元素的attachmentList字段（Array<Map<String,String>>）
 **/
public class analysis_json_post_info extends UDF {
    public String evaluate(String tags) {
        String output = "";
        List<String> picList = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(tags);
        for (int i = 0; i < jsonArray.length(); i++) {
            String cell = jsonArray.get(i).toString();
            JSONObject jsonObject = new JSONObject(cell);
            if(StringUtils.isBlank(jsonObject.getString("url"))) continue;
            picList.add(jsonObject.getString("url"));
        }
        if(CollectionUtils.isEmpty(picList)) return "";
        return JSON.toJSONString(picList);
    }


    public static void main(String[] args) {
        String test = "[{\"url\":\"\",\"name\":null,\"suffix\":null,\"firstFrame\":null,\"tranCodeTaskId\":null,\"type\":1,\"longTime\":0,\"tranCodeStatus\":null,\"sortIndex\":0,\"size\":0.0},{\"longTime\":0,\"firstFrame\":null,\"tranCodeTaskId\":null,\"sortIndex\":0,\"type\":1,\"suffix\":null,\"name\":null,\"tranCodeStatus\":null,\"size\":0.0,\"url\":\"\"},{\"tranCodeTaskId\":null,\"sortIndex\":0,\"longTime\":0,\"size\":0.0,\"url\":\"\",\"name\":null,\"suffix\":null,\"firstFrame\":null,\"tranCodeStatus\":null,\"type\":1}]";

        analysis_json_post_info result = new analysis_json_post_info();
        System.out.println(result.evaluate(test));
    }
}
