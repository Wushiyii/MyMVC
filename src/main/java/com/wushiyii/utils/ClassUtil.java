package com.wushiyii.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

@Slf4j
public final class ClassUtil {

    /**
     * file 形式 url 协议
     */
    public static final String FILE_PROTOCOL = "file";

    /**
     * jar 形式 url 协议
     */
    public static final String JAR_PROTOCOL = "jar";

    /**
     * 获取 classLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取 Class
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取包下类集合
     */
    public static Set<Class<?>> getPackageClass(String basePackage) {
        URL url = getClassLoader()
                .getResource(basePackage.replace(".", "/"));
        if (null == url) {
            throw new RuntimeException("无法获取项目路径文件");
        }
        try {
            if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)) {
                // 若为普通文件夹，则遍历
                File file = new File(url.getFile());
                Path basePath = file.toPath();
                return Files.walk(basePath)
                        .filter(path -> path.toFile().getName().endsWith(".class"))
                        .map(path -> getClassByPath(path, basePath, basePackage))
                        .collect(Collectors.toSet());
            } else if (url.getProtocol().equalsIgnoreCase(JAR_PROTOCOL)) {
                // 若在 jar 包中，则解析 jar 包中的 entry
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                return jarURLConnection.getJarFile()
                        .stream()
                        .filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                        .map(ClassUtil::getClassByJar)
                        .collect(Collectors.toSet());
            }
            return Collections.emptySet();
        } catch (IOException e) {
            log.error("load package error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 从 Path 获取 Class
     */
    private static Class<?> getClassByPath(Path classPath, Path basePath, String basePackage) {
        String packageName = classPath.toString().replace(basePath.toString(), "");
        String className = (basePackage + packageName)
                .replace("/", ".")
                .replace("\\", ".")
                .replace(".class", "");
        return loadClass(className);
    }

    /**
     * 从 jar 包获取 Class
     */
    private static Class<?> getClassByJar(JarEntry jarEntry) {
        String jarEntryName = jarEntry.getName();
        // 获取类名
        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
        return loadClass(className);
    }


    /**
     * String类型转换成对应类型
     *
     * @param type  转换的类
     * @param value 值
     * @return 转换后的Object
     */
    public static Object generateParameterObject(Class<?> type, Object value) {
        if (isPrimitive(type)) {
            if (Objects.isNull(value)) {
                return generateDefaultObject(type);
            }
            return getTypeMatchedObject(type, value);

        } else if (isFile(type)) {
            return value;
        } else {
            throw new RuntimeException("不支持的解析映射类型, type=" + type + ",value=" + value);
        }
    }

    /**
     * 返回基本数据类型的空值
     *
     * @param type 类
     * @return 对应的空值
     */
    public static Object generateDefaultObject(Class<?> type) {
        if (type.equals(int.class) || type.equals(double.class) ||
                type.equals(short.class) || type.equals(long.class) ||
                type.equals(byte.class) || type.equals(float.class)) {
            return 0;
        }
        if (type.equals(boolean.class)) {
            return false;
        }
        return null;
    }


    /**
     * 判定是否基本数据类型(包括包装类)
     *
     * @param type 类
     * @return 是否为基本数据类型
     */
    public static boolean isPrimitive(Class<?> type) {
        return type == boolean.class
                || type == Boolean.class
                || type == double.class
                || type == Double.class
                || type == float.class
                || type == Float.class
                || type == short.class
                || type == Short.class
                || type == int.class
                || type == Integer.class
                || type == long.class
                || type == Long.class
                || type == String.class
                || type == byte.class
                || type == Byte.class
                || type == char.class
                || type == Character.class;
    }

    public static boolean isFile(Class<?> type) {
        return type == File.class;
    }


    private static Object getTypeMatchedObject(Class<?> type, Object value) {
        try {
            if (isPrimitive(type)) {
                String strValue = (String) value;

                if (type.equals(int.class) || type.equals(Integer.class)) {
                    return Integer.parseInt(strValue);
                } else if (type.equals(String.class)) {
                    return strValue;
                } else if (type.equals(Double.class) || type.equals(double.class)) {
                    return Double.parseDouble(strValue);
                } else if (type.equals(Float.class) || type.equals(float.class)) {
                    return Float.parseFloat(strValue);
                } else if (type.equals(Long.class) || type.equals(long.class)) {
                    return Long.parseLong(strValue);
                } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                    return Boolean.parseBoolean(strValue);
                } else if (type.equals(Short.class) || type.equals(short.class)) {
                    return Short.parseShort(strValue);
                } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                    return Byte.parseByte(strValue);
                }
            } else if (value instanceof JSONObject) {
                return JSONObject.parseObject(String.valueOf(value), type);
            }

            return value;
        } catch (Exception e) {
            throw new RuntimeException(String.format("can not transfer value=%s to type=%s", value, type), e);
        }
    }

    @SneakyThrows
    public static Object generateBodyObject(Class<?> type, Map<String, Object> requestMap) {
        Object object = type.newInstance();

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (requestMap.containsKey(field.getName())) {
                setFiled(field, object, requestMap.get(field.getName()));
            }
        }
        return object;
    }

    @SneakyThrows
    private static void setFiled(Field field, Object object, Object value) {

        field.setAccessible(true);
        field.set(object, getTypeMatchedObject(field.getType(), value));
    }
}