package com.wushiyii.utils;

/**
 * @Author: wgq
 * @Date: 2022/2/8 15:34
 */
public class StringUtils {

    public static String trimSlash(String str) {
        if (isEmtpy(str)) {
            return str;
        }
        str = str.trim();
        if (!str.startsWith("/")) {
            str = "/" + str;
        }
        if (str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static boolean isEmtpy(String str) {
        return null == str || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmtpy(str);
    }

}
