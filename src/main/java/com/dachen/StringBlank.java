package com.dachen;

import org.apache.hadoop.hive.ql.exec.UDF;

public class StringBlank extends UDF {
    public boolean evaluate(String cs, boolean isBlank) {
        if (!isBlank) {
            return cs == null || cs.length() == 0;
        }
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public boolean evaluate(String cs) {
        return evaluate(cs, true);
    }

    public static void main(String[] args) {
        String a = "a";
        String b = "";
        String c = "  ";
        String d = "   \n";
        System.out.println(new StringBlank().evaluate(a));
        System.out.println(new StringBlank().evaluate(b));
        System.out.println(new StringBlank().evaluate(c));
        System.out.println(new StringBlank().evaluate(d));
    }
}
