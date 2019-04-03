package com.dachen;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dachen.IP2City.binarySearch;

public class IP2Province extends UDF {

    public String evaluate(String ip) throws IOException {

        List<String> map = new ArrayList();
        long[] start_from_index = new long[0];
        long[] end_to_index  = new long[0];
        Map<Long, String> provinceCache = new HashMap();
        long ipLong = 0L;

        //将IP转换成十进制
        ipLong = IP2City.getIpLong(ip, ipLong);

        if (map.size() == 0) {

            Configuration conf = new Configuration();
            String namenode = conf.get("fs.default.name");
            String url = namenode + "/jar/ip2addr.txt";
            FileSystem fs = FileSystem.get(conf);
            Path path = new Path(url);
            FSDataInputStream fsds = fs.open(path);
            InputStreamReader isr = new InputStreamReader(fsds);
            BufferedReader br = new BufferedReader(isr);

            try {
                String s;
                while (true) {
                    s = br.readLine();
                    if (s == null) {
                        break;
                    }
                    map.add(s);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            } finally {
                br.close();
            }

            start_from_index = new long[map.size()];
            end_to_index = new long[map.size()];

            for (int i = 0; i < map.size(); i++) {
                String[] token = map.get(i).split("\t");
                start_from_index[i] = Long.parseLong(token[0]);
                end_to_index[i] = Long.parseLong(token [1]);
            }
        }

        int ipindex = 0;

        if ((!provinceCache.containsKey(ipLong))) {
            ipindex = binarySearch(start_from_index, end_to_index, ipLong);
        }
        if (ipindex == 0) {
            return provinceCache.get(ipLong);
        } else if (ipindex == -1) {
            return "未知";
        }

        String[] location = map.get(ipindex).split("\t")[2].split("~");
        try {
            provinceCache.put(ipLong, location[1]);
            return location[1];
        } catch (Exception e) {
            return  "未知";
        }

    }

    public static void main(String[] args) throws IOException {
        IP2Province getIPLocation = new IP2Province();
        String ip = "112.122.64.0";

        String province = getIPLocation.evaluate(ip);

        System.out.printf("ip = %s, %s\n", ip, province);

    }
}