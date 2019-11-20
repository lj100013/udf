package com.dachen;

import com.dachen.util.ParseAddressUtil;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.List;

public class GetProvince extends UDF {
    public String evaluate(String address) {
        try {
            List<String> stringList = ParseAddressUtil.getStringList(address);
            String provinceSimilar = stringList.get(0);
            String provinceResult = ParseAddressUtil.isProvince(provinceSimilar);
            if (provinceResult != null) {
                return provinceResult;
            }
            String cityResult = ParseAddressUtil.isCity(provinceSimilar);
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

    public static void main(String[] args) {
        try {
            List<String> stringList = ParseAddressUtil.getStringList("市场部");
            String provinceSimilar = stringList.get(0);
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
