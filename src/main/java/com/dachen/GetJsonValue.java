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
            jsonStr = jsonStr.replace("\n", "").replace("\r", "");
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
            jsonStr = jsonStr.replace("\n", "").replace("\r", "");
            jsonType = getJosnType(jsonStr);
            if (jsonType != 2) {
                //map单独处理
                if (jsonType == 3) {
                    //对象类型字符串,数组无法返回
                    if (jsonStr.matches(objectRegex)) {
                        return null;
                    }
                    return getMatchPair(jsonStr, '{', '}', arrayIndex);
                }
                return "";
            }
            try {
                return JSONArray.parseArray(jsonStr).get(arrayIndex).toString();

            } catch (IndexOutOfBoundsException e) {
                return "";
            } catch (Exception e) {
                if (jsonStr.trim().indexOf('[', 1) < 0) {

                    return jsonStr.replace("[", "").replace("]", "").split(",")[arrayIndex].trim();
                }
                return getLayerMatchPair(jsonStr, '[', ']', 1).get(arrayIndex);
            }

        } catch (Exception ee) {
            return "";
        }
    }

    public String evaluate(String jsonStr, int arrayIndex, String key) {
        try {
            return evaluate(evaluate(jsonStr, arrayIndex), key);

        } catch (Exception e) {
            return null;
        }
    }


    //获取json对象指定key的value值
    public String getJsonObjectValue(JSONObject jsonObject, String key) {
        if (key.indexOf(".") == -1) {
            return jsonObject.get(key).toString();
        }
        Object object = jsonObject.get(key.substring(0, key.indexOf(".")));
        if (object == null || object.getClass().toString().endsWith("JSONArray")) {

            return null;
        }
        //有时会将字符串对象当成字符串处理
        if (object.getClass().toString().endsWith("String")) {
            object = JSONObject.parseObject(object.toString());

        }
        return getJsonObjectValue((JSONObject) object, key.substring(key.indexOf(".") + 1));

    }

    //获取map json串指定key的value值
    public String getMapValue(String jsonStr, String key) {
        try {
            //匹配key,忽略key大小写
            String[] keys = key.split("\\.");
            int layersize = keys.length;
            String mainKey = keys[layersize - 1];
            String regex = "(?i)" + mainKey + "\\s*" + "=";
            String returnStr = null;
            //获取当前对象指定层级的对象
            for (String str : getLayerMatchPair(jsonStr, '{', '}', layersize - 1)) {
                String params[] = str.split(regex);
                //未包含key
                if (params.length <= 1) {
                    continue;
                } else if (params.length == 2) {
                    //只存在一个key
                    if (params[1].trim().startsWith("{")) {
                        //key值是对象,返回对象
                        returnStr = getMatchPair(params[1].trim(), '{', '}', 0);
                        break;
                    }
                    //value值为字符串,逗号截取,字段未最后一个,需要清除 }
                    //key为字符串,返回字符串
                    returnStr = params[1].trim().split(",")[0].replace("}", "").trim();
                    break;

                } else {
                    //存在多个key,只取最外层的
                    str = str.trim().substring(1, str.length() - 1);
                    //取下一层,看是否包含key,包含则剔除
                    for (String endStr : getLayerMatchPair(str, '{', '}', 0)) {
                        //包含key,且对象外往前推的字符串只存在一个key,剔除,防止 a:{a:value}的情况
                        if (endStr.split(regex).length > 1 && str.substring(str.indexOf(endStr) - key.length() - 2, str.indexOf(endStr) + endStr.length()).split(regex).length == 2) {
                            str = str.replace(endStr, "");
                        }
                    }
                    //替换过后依旧存在相同的key,则为key.key的情况
                    if (str.split(regex).length > 2) {
                        for (String endStr : getLayerMatchPair(str, '{', '}', 0)) {
                            //包含key,且只包含一个key，剔除
                            if (endStr.split(regex).length == 2) {
                                return endStr;
                            }
                        }
                    }

                    params = str.split(regex);
                    if (params[1].trim().startsWith("{")) {
                        //key值是对象,返回对象
                        return getMatchPair(params[1].trim(), '{', '}', 0);
                    }
                    //value值为字符串,逗号截取,字段未最后一个,需要清除 }
                    //key为字符串,返回字符串
                    return params[1].trim().split(",")[0].replace("}", "").trim();
                }

            }
            return returnStr;
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
            if (jsonStr.matches(objectRegex)) {
                try {
                    //解析成功为 json对象
                    JSONObject.parseObject(jsonStr);
                    return 1;
                } catch (Exception ee) {
                    //解析失败,并且包含=,map对象
                    if (jsonStr.indexOf("=") != -1) {
                        return 3;
                    }
                    //无法识别的字符串类型
                    return 0;
                }
            } else if (jsonStr.matches(arrayRegex)) {
                try {
                    //解析成功为 json数组
                    JSONArray.parseArray(jsonStr);
                    return 2;
                } catch (Exception ee) {
                    //解析失败,并且包含=,map对象
                    if (jsonStr.indexOf("=") != -1) {
                        return 3;
                    }
                    return 2;
                }
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


    //json对象key小写
    public JSONObject toLowerKeyObject(JSONObject o1) {
        JSONObject o2 = new JSONObject();
        for (String key : o1.keySet()) {
            Object object = o1.get(key);

            //value值未null
            if (object == null) {
                o2.put(key.toLowerCase(), object);
                continue;
            }

            if (object.getClass().toString().endsWith("JSONObject")) {
                o2.put(key.toLowerCase(), toLowerKeyObject((JSONObject) object));
            } else if (object.getClass().toString().endsWith("JSONArray")) {
                if (o1.getJSONArray(key) == null || o1.getJSONArray(key).size() == 0 || o1.getJSONArray(key).get(0).getClass().toString().endsWith("String")) {
                    o2.put(key.toLowerCase(), object);
                }
                o2.put(key.toLowerCase(), toLowerKeyArray((JSONArray) o1.getJSONArray(key)));

            } else {
                try {
                    object = JSONObject.parseObject(object.toString());
                    //存在字符串对象无法识别的情况,需手动转换一下
                    o2.put(key.toLowerCase(), toLowerKeyObject((JSONObject) object));
                } catch (Exception e) {
                    o2.put(key.toLowerCase(), object);
                }

            }
        }
        return o2;
    }

    //数组key小写
    public JSONArray toLowerKeyArray(JSONArray o1) {
        JSONArray o2 = new JSONArray();
        for (int i = 0; i < o1.toArray().length; i++) {
            Object jArray = o1.get(i);
            if (jArray.getClass().toString().endsWith("JSONObject")) {
                //对象,调用对象key小写方法
                o2.add(toLowerKeyObject((JSONObject) jArray));
            } else if (jArray.getClass().toString().endsWith("JSONArray")) {
                //数组,递归,继续
                o2.add(toLowerKeyArray((JSONArray) jArray));
            } else {
                try {
                    jArray = JSONObject.parseObject(jArray.toString());
                    //存在字符串对象无法识别的情况,需手动转换一下
                    o2.add(toLowerKeyObject((JSONObject) jArray));
                } catch (Exception e) {
                    //字符串,无需转为小写
                    return o1;
                }

            }

        }
        return o2;
    }

    //获取指定字符串出现个数
    public int getOccurCount(String src, String find) {
        int o = 0;
        int index = -1;
        while ((index = src.indexOf(find, index)) > -1) {
            ++index;
            ++o;
        }
        return o;
    }

    //找到成对的指定字符包含的内容
    //index获取第几对,从0开始
    public String getMatchPair(String src, char start, char end, int index) {
        int number = src.length();
        index = index + 1;
        //成对个数
        int pair = getOccurCount(src, String.valueOf(start));
        //参数校验,不成对 或 成对个数小于获取第几对的值
        if (number < index || pair < index) {
            return null;
        }
        int startIndex = src.indexOf(start);
        int endIndex = 0;
        int startNum = 1;
        int endNum = 0;
        int pariNum = 0;
        for (int i = startIndex + 1; i < src.length(); i++) {
            //开始字符匹配,加一
            if (src.charAt(i) == start) {
                //开始=结束,已经成对,修改开始的索引值,获取下一对的开始
                if (startNum == endNum) {
                    startIndex = i;
                }
                startNum = startNum + 1;
            } else if (src.charAt(i) == end) {
                //结束字符匹配,加一
                endNum = endNum + 1;
                //开始字符数量=结束字符数量,已经成对,成对值+1
                if (startNum == endNum) {
                    pariNum = pariNum + 1;
                    //成对数量=需要获取的第几对,返回字符所在的索引值
                    if (pariNum == index) {
                        endIndex = i;
                        break;
                    }

                }
            }

        }
        if (endIndex == 0) {
            return null;
        }

        return src.substring(startIndex, endIndex + 1);

    }

    //获取指定匹配的内容
    //layerIndex 获取嵌套的第几层,从0开始,
    public List<String> getLayerMatchPair(String src, char start, char end, int layerIndex) {

        int i = 0;
        List<String> lists = new ArrayList<String>();
        while (getMatchPair(src, start, end, i) != null) {
            lists.add(getMatchPair(src, start, end, i));
            i = i + 1;
        }
        if (layerIndex == 0) {
            return lists;
        }
        List<String> lists2 = new ArrayList<String>();
        for (String list : lists) {
            for (String l : getLayerMatchPair(list.trim().substring(1, list.length() - 1), start, end, layerIndex - 1)) {
                lists2.add(l);

            }
        }
        return lists2;

    }

    public static void main(String[] args) {
        String json = "{\"hasinviteuser\": False, \"doctorid\": \"37535\", \"leftasktimes\": 3, \"familyuserid\": \"\", \"paytime\": 1573208276104L, \"relatedtype\": 0, \"unionid\": \"5dbfcd2cdfc3c0329106e89f\", \"patientname\": \"周秀英\", \"recordstatus\": 3, \"orderid\": 642427553230884864L, \"patientid\": \"5dc2c8b8d85cb42337dec849\", \"expectendtime\": 1571565342152L, \"referrerid\": \"\", \"starttime\": 1573268309448L, \"patientheadpic\": \"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIeic1cB1rvmSZdicNfQ7URXqJiaW2635GPRmVxibxLGJiaFkfvsc3cqwzoa7FFhhvUVHicwOPP2UvE0BIA/132\", \"ordertype\": 2, \"userid\": \"586905\"}";
        String json2 = "{'starttime': 1573268309448L}";
        String json3 = "{\n" +
                "  \"courseId\" : \"619493640888127488\"\n" +
                "}";
        String arrayjson = "[{\"nAme\":\"yang\",\"age\":9,\"addr\":{\"country\":\"中国\",\"city\":\"深圳\",\"compaNy\":[\"大辰\",\"玄关\"]}},{\"nAme\":\"LI\",\"age\":9,\"addr\":{\"country\":\"CHINAME\",\"city\":\"深圳\",\"compaNy\":[\"大辰2\",\"玄关2\"]}}]";
        String arrayjson2 = "[393124139952619520, 393130680873168896, 393133500489908224, 393135976521707520]";
        String arrayjson3 = "[[aaa,111],[bbb,222]]";
        String arrayjson4 = "[]";
        String arrayjson5 = "";
        String mapjson = "[{type=0, file={sizeStr=238 KB, suffix=pdf, file_id=o_1btf2b8f6li72gq16olnjdkhn11, type=application/pdf, file_name=“儿童晕厥诊断指南(2016年修订版)”解读（王成，2016）.pdf, size=244191, file_url=http://community.file.dachentech.com.cn/o_1btf2b8f6li72gq16olnjdkhn11,xxx={yyy=zzz}}}]";
        String mapjson2 = "{type=0, file={sizeStr=238 KB, suffix=pdf, file_id=o_1btf2b8f6li72gq16olnjdkhn11, type={type={aaa=bbb}}, file_name=“儿童晕厥诊断指南(2016年修订版)”解读（王成，2016）.pdf, size=244191, file_url=http://community.file.dachentech.com.cn/o_1btf2b8f6li72gq16olnjdkhn11,xxx={yyy=zzz}}}";
        System.out.println(arrayjson3.trim().indexOf("[", 2));
//        System.out.println(new GetJsonValue().evaluate(mapjson2, "file.type.type.aaa"));
        System.out.println(new GetJsonValue().evaluate(arrayjson2, 2));

        String a = "{\"_id\" : \"586d1b49f509e29a=;,efad942e\", \"bizId\" : \"586d157df509e2ad713f71d5\", \"toUserId\" : \"10347\", \"amount\" : \"0.01\", \"message\" : \"阳光普照1\", \"red_id\" : \"95161003011997696\", \"materialId\" : \"39\", \"redSendSuccess\" : true, \"msgSendSuccess\" : true, \"result\" : \"{\\\"code\\\":\\\"0000\\\",\\\"result\\\":{\\\"result\\\":\\\"0.01\\\",\\\"Count\\\":1,\\\"GroupId\\\":\\\"\\\",\\\"ID\\\":\\\"95161003011997696\\\",\\\"Message\\\":\\\"阳光普照1\\\",\\\"Recipient\\\":\\\"10347\\\"},\\\"message\\\":\\\"操作成功\\\",\\\"request_id\\\":\\\"cc724bc0038d49a59a4b1959ec45972a\\\"}\", \"createDate\" : \"2017-01-04T23:56:56.000+0000\"}";
//        System.out.println(new GetJsonValue().evaluate(a, "result.resulT.id"));

    }
}
