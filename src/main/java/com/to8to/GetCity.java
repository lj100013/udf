package com.to8to;

import com.to8to.util.ParseAddressUtil;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.junit.Test;

import java.util.List;

public class GetCity extends UDF {
    public static String municipalities = "北京市,上海市,天津市,重庆市";

    public String evaluate(String address) {
        try {
            List<String> stringList = ParseAddressUtil.getStringList(address);
            String similar = null;
            String result = null;

            //先根据县级地址,取省市
            for (String word : stringList) {
                if (word.indexOf("县") != -1 && word.length() > 1) {
                    List<String> provinceAndCity = ParseAddressUtil.areaToProvinceAndCity.get(word);
                    if (provinceAndCity != null) {
                        String province = provinceAndCity.get(0);
                        String city = provinceAndCity.get(1);
                        //是直辖市,city直接写直辖市
                        if (municipalities.indexOf(province) != -1) {
                            return province;
                        }
                        return city;
                    }
                }
            }

            //分词长度大于1,并且分的词长度大于一,先取第二个
            if (stringList.size() > 1 && stringList.get(1).length() > 1) {
                similar = stringList.get(1);
                result = getResultCity(similar);
                String provin = ParseAddressUtil.isProvince(stringList.get(0));

                //未匹配到city,尝试取前两位去匹配
                if (result == null && similar.length() > 3) {
                    result = getResultCity(similar.substring(0, 2));

                }
                // 存在对应的city,必须保证province相同
                if (result != null && provin != null && ParseAddressUtil.cityToProvince.get(result).equals(provin)) {

                    return result;

                }
            }

            //没有省份的情况
            similar = stringList.get(0);

            //分词长度为1时,取后一个的第一个字符
            if (similar.length() == 1 && stringList.size() > 1) {
                similar = similar + stringList.get(1).substring(0, 1);
            }
            result = getResultCity(similar);
            String provin = ParseAddressUtil.isProvince(similar);

            //不能既是省又是市的情况
            if (result != null && (provin == null || municipalities.indexOf(provin) != -1)) {
                return result;
            }

            result = getResultCity(similar.substring(0, 2));
            if (result != null && (provin == null || municipalities.indexOf(provin) != -1)) {
                return result;
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getResultCity(String address) {
        String result = null;
        if (address.indexOf("市") != -1) {
            //包含市,截取市以及市以前的值,防止分词未分开的情况
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

    @Test
    public void test() {
        System.out.println(evaluate("新乡心一区"));
    }

    public static void main(String[] args) {
        try {
            List<String> stringList = ParseAddressUtil.getStringList("潮汕心一区-神经内科-心血管内科-消化内科");
            String similar = null;
            String result = null;
            //先根据县级地址,取省市
            for (String word : stringList) {
                if (word.indexOf("县") != -1 && word.length() > 1) {
                    List<String> provinceAndCity = ParseAddressUtil.areaToProvinceAndCity.get(word);
                    if (provinceAndCity != null) {
                        String province = provinceAndCity.get(0);
                        String city = provinceAndCity.get(1);
                        //是直辖市,city直接写直辖市
                        if (municipalities.indexOf(province) != -1) {
                            System.out.println(province);
                            return;
                        }
                        System.out.println(city);
                        return;
                    }
                }
            }
            if (stringList.size() > 1 && stringList.get(1).length() > 1) {
                similar = stringList.get(1);
                result = getResultCity(similar);
                String provin = ParseAddressUtil.isProvince(stringList.get(0));
                // 存在对应的city,必须保证province相同
                if (result != null && provin != null && ParseAddressUtil.cityToProvince.get(result).equals(provin)) {
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
