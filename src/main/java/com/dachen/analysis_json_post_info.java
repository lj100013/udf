package com.dachen;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author luoianun
 * @create 2018-11-14 11:40
 * @desc 解析basepost.post_info集合中cards數組的第一個元素的attachmentList字段（Array<Map<String,String>>）
 **/
public class analysis_json_post_info extends UDF {
    public String evaluate(String tags) {
        String output = "";
        JSONArray jsonArray = new JSONArray(tags);
        for (int i = 0; i < jsonArray.length(); i++) {
            String cell = jsonArray.get(i).toString();
            JSONObject jsonObject = new JSONObject(cell);
            String url = jsonObject.get("url").toString();
            output = output + url + ",";
        }
        if (output.endsWith(",")) {
            output = output.substring(0, output.length() - 1);
        }

//        JSON.toJSONString( output.split(","));

        return JSON.toJSONString(output.split(","));
    }


    public static void main(String[] args) {
        String test = "[{\"url\":\"http://community.file.dachentech.com.cn/4265d9dab3ef4db4981bc7e875c715c7\",\"name\":null,\"suffix\":null,\"firstFrame\":null,\"tranCodeTaskId\":null,\"type\":1,\"longTime\":0,\"tranCodeStatus\":null,\"sortIndex\":0,\"size\":0.0},{\"longTime\":0,\"firstFrame\":null,\"tranCodeTaskId\":null,\"sortIndex\":0,\"type\":1,\"suffix\":null,\"name\":null,\"tranCodeStatus\":null,\"size\":0.0,\"url\":\"http://community.file.dachentech.com.cn/0c7829196a8c41f3bf607fc83ea2445e\"},{\"tranCodeTaskId\":null,\"sortIndex\":0,\"longTime\":0,\"size\":0.0,\"url\":\"http://community.file.dachentech.com.cn/a0dc0c6ba21247d78f1fe71c36f4fcbe\",\"name\":null,\"suffix\":null,\"firstFrame\":null,\"tranCodeStatus\":null,\"type\":1}]";


        analysis_json_post_info result = new analysis_json_post_info();
        System.out.println(result.evaluate(test));
    }
}
