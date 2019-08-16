package com.dachen;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.ArrayList;
import java.util.List;

public class GetJsonValue extends UDF {
    private String arrayRegex = "\\s*\\[.*]\\s*";
    private String objectRegex = "\\s*\\{.*}\\s*";
    private int jsonType = -1;

    public String evaluate(String jsonStr, String key) {
        try {
            jsonType = getJosnType(jsonStr);

            //数组无法获取指定key,无法识别的字符串类型
            if (jsonType == 2 || jsonType == 0) {
                return null;
            }

            //map单独处理
            if (jsonType == 3) {
                //map 数组类型,无法返回
                if (jsonStr.matches(arrayRegex)) {
                    return null;
                }
                return getMapValue(jsonStr, key.toLowerCase().trim());

            }
            //key转小写
            JSONObject object = toLowerKeyObject(JSONObject.parseObject(jsonStr));
            return getJsonObjectValue(object, key.toLowerCase().trim());
        } catch (Exception e) {
            return null;
        }
    }


    public String evaluate(String jsonStr, int arrayIndex) {
        try {
            jsonType=getJosnType(jsonStr);
            if (getJosnType(jsonStr) != 2) {
                //map单独处理
                if (jsonType == 3) {
                    //对象类型字符串,数组无法返回
                    if (jsonStr.matches(objectRegex)) {
                        return null;
                    }
                    return getMatchPair(jsonStr,'{','}',arrayIndex);
                }
                return null;
            }
            try {
                return JSONArray.parseArray(jsonStr).get(arrayIndex).toString();

            } catch (IndexOutOfBoundsException e) {
                return null;
            } catch (Exception e) {
                return jsonStr.replace("[", "").replace("]", "").trim().split(",")[arrayIndex];
            }

        } catch (Exception ee) {
            return null;
        }
    }

    public String evaluate(String jsonStr, int arrayIndex, String key) {
        try {
            return evaluate(evaluate(jsonStr, arrayIndex), key);

        } catch (Exception e) {
            return null;
        }
    }


    public String getJsonObjectValue(JSONObject jsonObject, String key) {
        if (key.indexOf(".") == -1) {
            return jsonObject.get(key).toString();
        }
        Object object = jsonObject.get(key.substring(0, key.indexOf(".")));
        if (object == null || object.getClass().toString().endsWith("JSONArray") || object.getClass().toString().endsWith("String")) {
            return null;
        }
        return getJsonObjectValue((JSONObject) object, key.substring(key.indexOf(".") + 1));

    }

    public String getMapValue(String jsonString, String key) {
        try {
            String regex = "(?i)" + key + "\\s*" + "=";
            //未嵌套key,且在json串中不存在,返回空

            if (key.indexOf(".") == -1) {
                String[] params = jsonString.split(regex);
                if (params.length == 1) {
                    return null;
                } else if (params.length == 2) {
                    //value值为对象
                    if(params[1].trim().startsWith("{")){
                        return getMatchPair(params[1].trim(),'{','}',0);
                    }
                    //value值为字符串,逗号截取,字段未最后一个,需要清除 }
                    return params[1].trim().split(",")[0].replace("}","").trim();
                } else {
                    //多个相同的key值情况
                    jsonString=jsonString.trim().substring(1,jsonString.length()-1);
                    while (jsonString.indexOf("{")!=-1){
                        //替换所有的{} 值,只取最外层的字段
                        jsonString=jsonString.replace(getMatchPair(jsonString,'{','}',0),"").trim();
                    }
                    return jsonString.trim().split(regex)[1].trim().split(",")[0].trim();
                }
            }
            String[] keys = key.split("\\.");
            for(int i=0;i<keys.length-1;i++){
                //去掉最外层大括号
//            jsonString=jsonString.trim().substring(1,jsonString.length()-1);
                List<String> list=new ArrayList<>();
                for(int j=0;j<getOccurCount(jsonString,"{");j++){
                   String json= getMatchPair(jsonString,'{','}',j);
                   if(json!=null && json.indexOf(keys[i])!=-1){
                       list.add(json);
                   }
                }
                getMatchPair(jsonString,'{','}',i);
            }
            return getMapValue(jsonString.split("(?i)" +keys[0].trim()+ "\\s*" + "=")[1], keys[1].trim());
        } catch (Exception e) {
            return null;
        }


    }


/*
    获取传入值得类别
    0:无法识别
    1:json {}
    2:json数组 []
    3:map
*/

    public int getJosnType(String jsonStr) {

        try {
            if (jsonStr.matches(objectRegex) && jsonStr.indexOf("=")==-1) {
                return 1;
            } else if (jsonStr.matches(arrayRegex) && jsonStr.indexOf("=")==-1) {
                return 2;
            } else if (isMap(jsonStr)) {
                return 3;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    //判断是否为map类型
    public boolean isMap(String jsonStr) {
        //没有 =  []  {} 号,不是map类型
        if (jsonStr.indexOf("=") == -1 || (!jsonStr.trim().matches(arrayRegex) && !jsonStr.trim().matches(objectRegex))) {
            return false;
        }
        return true;
    }


    public static JSONObject toLowerKeyObject(JSONObject o1) {
        JSONObject o2 = new JSONObject();
        for (String key : o1.keySet()) {
            Object object = o1.get(key);
            if (object.getClass().toString().endsWith("JSONObject")) {
                o2.put(key.toLowerCase(), toLowerKeyObject((JSONObject) object));
            } else if (object.getClass().toString().endsWith("JSONArray")) {
                if (o1.getJSONArray(key) == null || o1.getJSONArray(key).size() == 0 || o1.getJSONArray(key).get(0).getClass().toString().endsWith("String")) {
                    o2.put(key.toLowerCase(), object);
                }
                o2.put(key.toLowerCase(), toLowerKeyArray((JSONArray) o1.getJSONArray(key)));

            } else {
                o2.put(key.toLowerCase(), object);
            }
        }
        return o2;
    }

    public static JSONArray toLowerKeyArray(JSONArray o1) {
        JSONArray o2 = new JSONArray();
        for (int i = 0; i < o1.toArray().length; i++) {
            Object jArray = o1.get(i);
            if (jArray.getClass().toString().endsWith("JSONObject")) {
                o2.add(toLowerKeyObject((JSONObject) jArray));
            } else if (jArray.getClass().toString().endsWith("JSONArray")) {
                o2.add(toLowerKeyArray((JSONArray) jArray));
            } else {
                return o1;
            }

        }
        return o2;
    }

    public  int getOccurCount(String src, String find) {
        int o = 0;
        int index = -1;
        while ((index = src.indexOf(find, index)) > -1) {
            ++index;
            ++o;
        }
        return o;
    }

    //找到成对的指定字符包含的内容
    public  String getMatchPair(String src,char start, char end,int index){
        int number=src.length();
        //成对个数
        int pair=getOccurCount(src,String.valueOf(start));
        //参数校验,不成对 或 成对个数小于获取第几对的值
        if(number<index  ||  pair<index){
            return null;
        }
        int startIndex=src.indexOf(start);
        int endIndex=0;
        int startNum=1;
        int endNum=0;
        int pariNum=0;
        for(int i=startIndex+1;i<src.length();i++){
            //开始字符匹配,加一
            if(src.charAt(i)==start){
                //开始=结束,已经成对,修改开始的索引值,获取下一对的开始
                if(startNum==endNum){
                    startIndex=i;
                }
                startNum=startNum+1;
            }else if(src.charAt(i)==end){
                //结束字符匹配,加一
                endNum=endNum+1;
            }
            //开始字符数量=结束字符数量,已经成对,成对值+1
            if(startNum==endNum ){
                pariNum=pariNum+1;
                //成对数量=需要获取的第几对,返回字符所在的索引值
                if(pariNum==index){
                    endIndex=i;
                    break;
                }

            }
        }
        if(endIndex==0){
            return null;
        }

        return src.substring(startIndex,endIndex+1);

    }
    public static void main(String[] args) {
        String json = "{\"nAme\":\"yang\",\"age\":9,\"addr\":{\"country\":\"中国\",\"city\":\"深圳\",\"compaNy\":[\"大辰\",\"玄关\"]}}";
        String json2 = "{name:\"yang\",age:10}";
        String json3 = "{}";
        String arrayjson = "[{\"nAme\":\"yang\",\"age\":9,\"addr\":{\"country\":\"中国\",\"city\":\"深圳\",\"compaNy\":[\"大辰\",\"玄关\"]}},{\"nAme\":\"LI\",\"age\":9,\"addr\":{\"country\":\"CHINAME\",\"city\":\"深圳\",\"compaNy\":[\"大辰2\",\"玄关2\"]}}]";
        String arrayjson2 = "[\"a\",\"b\",3]";
        String arrayjson3 = "[[\"aaa\",111],[\"bbb\",222]]";
        String arrayjson4 = "[]";
        String arrayjson5 = "";
        String mapjson = "[{type=0, file={sizeStr=238 KB, suffix=pdf, file_id=o_1btf2b8f6li72gq16olnjdkhn11, type=application/pdf, file_name=“儿童晕厥诊断指南(2016年修订版)”解读（王成，2016）.pdf, size=244191, file_url=http://community.file.dachentech.com.cn/o_1btf2b8f6li72gq16olnjdkhn11,xxx={yyy=zzz}}}]";
        String mapjson2 = "{type=0, file={sizeStr=238 KB, suffix=pdf, file_id=o_1btf2b8f6li72gq16olnjdkhn11, type=application/pdf, file_name=“儿童晕厥诊断指南(2016年修订版)”解读（王成，2016）.pdf, size=244191, file_url=http://community.file.dachentech.com.cn/o_1btf2b8f6li72gq16olnjdkhn11}}";

        System.out.println(new GetJsonValue().evaluate(mapjson2, "file.xxx.yyy"));
        System.out.println(new GetJsonValue().evaluate(mapjson, 0,"file"));
//        System.out.println(new GetJsonValue().evaluate(new GetJsonValue().evaluate(json, "addR.compaNy"), 1));
//        System.out.println(getMatchPair(arrayjson,'[',']',0));
//        System.out.println("jsonString".substring(1,"jsonString".length()));
    }
}
