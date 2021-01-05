package com.to8to;


import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class regexp_filter extends UDF

{
    private String[] split;


    public String evaluate(String tags) {
        List<String> resList = new ArrayList<String>();
        if (tags == null || "".equals(tags)) {
            return "";
        }

        split = tags.split(",");
        for (String ptag : split) {
            ptag.trim();
            ptag = ptag.replace("|;",",").replace("|",",").replace("、", ",")
                    .replace("\"", "").replace(")", "")
                    .replace("\t", "").replace(";",",").replace("；",",")
                    .replace("、", ",").replace("等","");
            Pattern p1 = Pattern.compile("(?<=\\()[^\\)]+");
            Pattern p2 = Pattern.compile("^[A-Z]");
            Pattern p0 = Pattern.compile("[A-Za-z]");

            for(String res: ptag.split(",")){
                res=res.trim();
                Matcher m1 = p1.matcher(res);
                Matcher m0 = p0.matcher(res);
                if (m1.find()) {
                    String r1 = m1.group();
                    resList.add(ptag.split("\\(")[0]);
                    Matcher m2 = p2.matcher(r1);
                    if (m2.find()) {
                        resList.add(r1);
                    }
                }else if(m0.find()){
                    resList.add(res);
                }else if(res.contains("，")){
                    for(String k:res.split("，")){
                        resList.add(k.replace(" ",""));
                    }
                }
                else{
                    for(String rs : res.split(" ")){
                        resList.add(rs.split("（")[0]);
                    }
                }
            }
        }
        return StringUtils.strip(removeEmptyList(resList).toString().trim(),"[]");
    }

    public  List removeEmptyList(List<String> list) {
        List list1 = new ArrayList();
        if(list==null||list.size()<=0)
            return null;
        for(int i=0;i<list.size();i++) {
            String res = list.get(i);
            if(res!=null && res.length()>0)
                list1.add(res);
        }
        return list1;
    }

    public static void main(String[] args) {
        String tag = "林  薇，纪  征，齐贺文，司伟，李艳军，邢爱君，马  青，张  辉，徐 勇 等 ,中华医学会消化内镜学分会；[2]中国抗癌协会肿瘤内镜学专业委员会,李焕琼主任(Archer)、欧阳钦;Rakesh Tandon;KL Goh;潘国宗;KM Fock;Claudio Fiocchi;SK Lam;萧树东、、欧阳钦;Rakesh Tandon;KL Goh;， 石群立|;   陈自谦|;   李苏建|;许建|;吴新生|;储成奇|李建生，张德奎、、、李瑞英、齐国卿、吴志平（主持人：王祥）,基层医院;心脏疾病;运动心肺功能;自我管理;康复治疗,archer test,山西省太原市中心医院消化科主任医师 杨林承、李莹、徐也、张承铎、张云霄、周乐群管理员（118）赵新颖，何星星，唐茹琦，杨玲，陈倩，雷宇，刘红艳，宴维";
        regexp_filter getCmsID = new regexp_filter();
        System.out.println(getCmsID.evaluate(tag));
    }

}
