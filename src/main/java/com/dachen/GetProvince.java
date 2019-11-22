package com.dachen;

import com.dachen.util.ParseAddressUtil;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.junit.Test;

import java.util.List;

public class GetProvince extends UDF {
    public static String municipalities = "北京市,上海市,天津市,重庆市";

    public String evaluate(String address) {
        try {
            List<String> stringList = ParseAddressUtil.getStringList(address);

            for (String word : stringList) {
                if (word.indexOf("县") != -1 && word.length() > 1) {
//                    if (ParseAddressUtil.areaToProvinceAndCity.size() == 0) {
//                        return "hhh";
//                    }

                    List<String> provinceAndCity = ParseAddressUtil.areaToProvinceAndCity.get(word);
                    if (provinceAndCity != null) {
                        return provinceAndCity.get(0);
                    }
                }
            }

            String provinceSimilar = stringList.get(0);
            String provinceResult = ParseAddressUtil.isProvince(provinceSimilar);
            if (provinceResult != null) {
                return provinceResult;
            }

            //省份未匹配到,尝试匹配市
            String cityResult = ParseAddressUtil.isCity(provinceSimilar);
            if (cityResult != null) {
                //不为null,证明是地级市,需获取对应的省份
                return ParseAddressUtil.cityToProvince.get(cityResult);
            }

            //未找到省份和市,尝试截取前两位去匹配省
            provinceResult = ParseAddressUtil.isProvince(provinceSimilar.substring(0, 2));
            if (provinceResult != null) {
                return provinceResult;
            }

            //未找到省份和市,尝试截取前两位去匹配市
            cityResult = ParseAddressUtil.isCity(provinceSimilar.substring(0, 2));
            if (cityResult != null) {
                //不为null,证明是地级市,需获取对应的省份
                return ParseAddressUtil.cityToProvince.get(cityResult);
            }

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Test
    public void test() {
        System.out.println(evaluate("青岛大学附属医院(市南院区)-消化内科"));
    }

    public static void main(String[] args) {
        try {
            List<String> stringList = ParseAddressUtil.getStringList("苏州大学附属第一医院精神心理科_体验1-心血管内科-消化内科");
            String provinceSimilar = stringList.get(0);
            //先根据县级地址,取省市
            for (String word : stringList) {
                if (word.indexOf("县") != -1 && word.length() > 1) {
                    List<String> provinceAndCity = ParseAddressUtil.areaToProvinceAndCity.get(word);
                    if (provinceAndCity != null) {
                        System.out.println(provinceAndCity.get(0));
                        return;
                    }
                }
            }
            if (ParseAddressUtil.isProvince(provinceSimilar) != null) {
                System.out.println(ParseAddressUtil.isProvince(provinceSimilar));
                return;
            }
            if (ParseAddressUtil.isCity(provinceSimilar) != null) {
                //不为null,证明是地级市,需获取对应的省份

                System.out.println(ParseAddressUtil.cityToProvince.get(ParseAddressUtil.isCity(provinceSimilar)));
                return;
            }
            System.out.println(provinceSimilar);

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
