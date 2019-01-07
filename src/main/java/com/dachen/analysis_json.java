package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONArray;
import org.json.JSONObject;



/**
 * @author luoianun
 * @create 2018-06-29 16:45
 * @desc 解析health.t_survey_answer集合的answerList字段的数据
 **/
public class analysis_json extends UDF{
    public String evaluate(String tags) {
        String output ="";
        JSONArray jsonArray = new JSONArray(tags);
        for(int i =0; i < jsonArray.length() ; i++)
        {
            String cell = jsonArray.get(i).toString();
            JSONObject jsonObject = new JSONObject(cell);
            String question = jsonObject.get("question").toString();
            String options = jsonObject.get("options").toString();
            JSONObject questionJsonObject = new JSONObject(question);
            JSONArray optionsJsonArray = new JSONArray(options);
            String questionSeq = questionJsonObject.get("seq").toString();
            output+="题目"+questionSeq+":";
            for(int j = 0 ; j < optionsJsonArray.length() ; j++)
            {
                String cell2 = optionsJsonArray.get(j).toString();
                JSONObject jsonObject1 = new JSONObject(cell2);
                if(jsonObject1.has("seq")) {
                    String optionsSeq = jsonObject1.get("seq").toString();
                    output += optionsSeq + "、";
                }
            }
            if(output.endsWith("、"))
            {
                output = output.substring(0,output.length()-1);
            }
            output+="|";


        }
        if(output.endsWith("|"))
        {
            output = output.substring(0,output.length()-1);
        }

        return output;
    }

    public static void main(String[] args) {
        String str ="[ { \"question\" : { \"seq\" : 1 , \"name\" : \"以下研究不属于波依定的是\" , \"type\" : 1} , \"options\" : [ { \"seq\" : 1 , \"name\" : \"HOT研究\"}]} , { \"question\" : { \"seq\" : 2 , \"name\" : \"以下药物中血管/心脏选择性最高的CCB是\" , \"type\" : 1} , \"options\" : [ { \"seq\" : 2 , \"name\" : \"地尔硫卓\"}]} , { \"question\" : { \"seq\" : 3 , \"name\" : \"波依定不会引起下列哪一种副反应\" , \"type\" : 1} , \"options\" : [ { \"seq\" : 2 , \"name\" : \"面红\"}]} , { \"question\" : { \"seq\" : 4 , \"name\" : \"波依定服用方便，可以掰开或咀嚼服用。\" , \"type\" : 1} , \"options\" : [ { \"seq\" : 1 , \"name\" : \"对\"}]} , { \"question\" : { \"seq\" : 5 , \"name\" : \"波依定的通用名是（）。\" , \"type\" : 2} , \"options\" : [ { \"name\" : \"v 哈哈\"}]} , { \"question\" : { \"seq\" : 6 , \"name\" : \"波依定的规格是（）。\" , \"type\" : 2} , \"options\" : [ { \"name\" : \"v 不\"}]} , { \"question\" : { \"seq\" : 7 , \"name\" : \"波依定的适应症是（）,（）。\" , \"type\" : 2} , \"options\" : [ { \"name\" : \"和\"}]} , { \"question\" : { \"seq\" : 8 , \"name\" : \"眼睛在看远时，睫状肌处于状态，使晶状体悬韧带保持一定张力，晶状体在悬韧带的牵引下，其形状相对变\" , \"type\" : 1} , \"options\" : [ { \"seq\" : 1 , \"name\" : \"松弛、凸\"}]} , { \"question\" : { \"seq\" : 9 , \"name\" : \"眼球的内容物包括睫状体、晶状体、视网膜。\" , \"type\" : 1} , \"options\" : [ { \"seq\" : 1 , \"name\" : \"对\"}]}]";
        analysis_json a = new analysis_json();
        System.out.println( a.evaluate(str));
       ;
    }


}
