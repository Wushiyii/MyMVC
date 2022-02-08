package com.wushiyii.utils;

/**
 * @Author: wgq
 * @Date: 2022/2/8 15:34
 */
public class StringUtils {

    public static String trimSlash(String str) {
        if (str.startsWith("/")) {
            str = str.substring(1);
        }
        if (str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


}
