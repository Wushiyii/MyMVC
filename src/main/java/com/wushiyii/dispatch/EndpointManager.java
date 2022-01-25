package com.wushiyii.dispatch;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.wushiyii.annotation.DELETE;
import com.wushiyii.annotation.GET;
import com.wushiyii.annotation.POST;
import com.wushiyii.annotation.PUT;
import com.wushiyii.dto.Constants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * @Author: wgq
 * @Date: 2022/1/22 15:20
 */
@Slf4j
public class EndpointManager {

    private final static Table<String, String, EndpointMetaInfo> endpointTable = HashBasedTable.create();

    // 不用考虑并发场景，只有启动时for循环注册一次
    @SneakyThrows
    public static void register(Class<?> clazz, Method declaredMethod) {
        String[] multiPath = {};
        String method = "";
        if (declaredMethod.isAnnotationPresent(GET.class)) {
            multiPath = declaredMethod.getAnnotation(GET.class).path();
            method = Constants.GET;
        } else if (declaredMethod.isAnnotationPresent(POST.class)) {
            multiPath = declaredMethod.getAnnotation(POST.class).path();
            method = Constants.POST;
        } else if (declaredMethod.isAnnotationPresent(DELETE.class)) {
            multiPath = declaredMethod.getAnnotation(DELETE.class).path();
            method = Constants.DELETE;
        } else if (declaredMethod.isAnnotationPresent(PUT.class)) {
            multiPath = declaredMethod.getAnnotation(PUT.class).path();
            method = Constants.PUT;
        }

        for (String path : multiPath) {
            EndpointMetaInfo metaInfo = EndpointMetaInfo.builder()
                    .path(path)
                    .endpointObject(clazz.newInstance())
                    .method(declaredMethod)
                    .build();
            register0(method, path, metaInfo);
        }
    }


    // 不用考虑并发场景，只有启动时for循环注册一次
    private static void register0(String method, String path, EndpointMetaInfo endpointMetaInfo) {
        log.info("Register endpoint method={} path={}", method, path);
        if (endpointTable.contains(method, path)) {
            throw new RuntimeException(String.format("[MyMVC] path already registered, method=%s, path=%s, metaInfo=%s", method, path, endpointMetaInfo));
        }
        endpointTable.put(method, path, endpointMetaInfo);
    }


    public static EndpointMetaInfo getEndpoint(String method, String path) {

        if (!endpointTable.contains(method, path)) {
            throw new RuntimeException(String.valueOf(HttpServletResponse.SC_NOT_FOUND));
        }
        return endpointTable.get(method, path);
    }


}
