package com.dachen;

import com.dachen.util.ParseAddressUtil;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.List;

public class GetCity extends UDF {
    public String evaluate(String address) {
        try {
            List<String> stringList = ParseAddressUtil.getStringList(address);
            //直辖市特殊处理
            String similar = null;
            String result = null;
            //分词长度大于1,并且分的词长度大于一,先取第二个
            if (stringList.size() > 1 && stringList.get(1).length() > 1) {
                similar = stringList.get(1);
                //包含市,截取市以及市以前的值,防止分词未分开的情况
                result = getResultCity(similar);
                if (result != null) {
                    return result;

                }
            }

            //没有省份的情况
            similar = stringList.get(0);
            result = getResultCity(similar);
            if (result != null) {
                return result;
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getResultCity(String address) {
        String municipalities = "北京市,上海市,天津市,重庆市";
        String result = null;
        if (address.indexOf("市") != -1) {
            address = address.substring(0, address.indexOf("市") + 1);
            //截取后的值长度为1,直接返回
            if (address.length() < 2) {
                return null;
            }
        }

        //直辖市判断
        if (municipalities.indexOf(address) != -1) {
            if (address.endsWith("市")) {
                return address;

            }
            return address + "市";

        }

        //判断是否为城市
        result = ParseAddressUtil.isCity(address);
        if (result != null) {
            return result;
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            String municipalities = "北京市,上海市,天津市,重庆市";
            List<String> stringList = ParseAddressUtil.getStringList("百色妇女儿童医疗中心精神心理科_体验1");
            String similar = null;
            String result = null;
            if (stringList.size() > 1 && stringList.get(1).length() > 1) {
                similar = stringList.get(1);
                //包含市,截取市以及市以前的值,防止分词未分开的情况
                result = getResultCity(similar);
                if (result != null) {
                    System.out.println(result);
                    return;

                }
            }

            //没有省份的情况
            similar = stringList.get(0);
            result = getResultCity(similar);
            if (result != null) {
                System.out.println(result);
                return;
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
