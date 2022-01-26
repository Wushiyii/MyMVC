package com.wushiyii.config;

import java.util.Objects;

/**
 * @Author: wgq
 * @Date: 2022/1/24 15:11
 */
public class MyMVCConfiguration {


    private static int port = 8080;

    private static String contextPath = "";

    private static String resourcePath = "src/main/resources/";

    private static String basePackage;

    private static Class<?> startClass;

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        MyMVCConfiguration.port = port;
    }

    public static String getContextPath() {
        return contextPath;
    }

    public static void setContextPath(String contextPath) {
        if (Objects.isNull(contextPath)) {
            return;
        }
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        MyMVCConfiguration.contextPath = contextPath;
    }

    public static String getResourcePath() {
        return resourcePath;
    }

    public static void setResourcePath(String resourcePath) {
        MyMVCConfiguration.resourcePath = resourcePath;
    }

    public static String getBasePackage() {
        return basePackage;
    }

    public static void setBasePackage(String basePackage) {
        MyMVCConfiguration.basePackage = basePackage;
    }

    public static Class<?> getStartClass() {
        return startClass;
    }

    public static void setStartClass(Class<?> startClass) {
        MyMVCConfiguration.startClass = startClass;
        MyMVCConfiguration.setBasePackage(startClass.getPackage().getName());
    }
}
