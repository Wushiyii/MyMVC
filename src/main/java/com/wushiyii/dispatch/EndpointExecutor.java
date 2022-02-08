package com.wushiyii.dispatch;

import com.wushiyii.annotation.BODY;
import com.wushiyii.annotation.PARAM;
import com.wushiyii.utils.ClassUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @Author: wgq
 * @Date: 2022/1/25 13:59
 */
@Slf4j
public class EndpointExecutor {


    private static final Set<String> OK_METHOD_SET = new HashSet<>();


    @SneakyThrows
    public static Object invokeEndpoint(EndpointMetaInfo endpoint, Map<String, Object> paramMap) {


        List<Object> parameters = getReflectParameters(endpoint, paramMap);
        Object result;
        if (parameters.size() > 0) {
            result = endpoint.getMethod().invoke(endpoint.getEndpointObject(), parameters.toArray());
        } else {
            result = endpoint.getMethod().invoke(endpoint.getEndpointObject());
        }
        return result;
    }

    public static List<Object> getReflectParameters(EndpointMetaInfo endpoint, Map<String, Object> requestMap) {

        if (endpoint.getMethod().getParameters().length == 0) {
            return Collections.emptyList();
        }

        Parameter[] parameters = endpoint.getMethod().getParameters();
        Class<?>[] parameterTypes = endpoint.getMethod().getParameterTypes();
        List<Object> objectList = new ArrayList<>();

        checkRequestType(endpoint.getMethod());

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> type = parameterTypes[i];

            PARAM pinedParam = param.getAnnotation(PARAM.class);
            BODY pinedBody = param.getAnnotation(BODY.class);

            if (Objects.nonNull(pinedParam) && requestMap.containsKey(pinedParam.value())) {
                objectList.add(ClassUtil.generateParameterObject(type, requestMap.get(pinedParam.value())));
            } else if (Objects.nonNull(pinedBody)) {
                objectList.add(ClassUtil.generateBodyObject(type, requestMap));
            } else {
                objectList.add(ClassUtil.generateDefaultObject(type));
            }
        }


        return objectList;
    }

    private static void checkRequestType(Method method) {
        if (OK_METHOD_SET.contains(method.toString())) {
            return;
        }

        Parameter[] parameters = method.getParameters();

        //接收入参的@BODY 注解只能同时用一个
        int bodyAnnotationCount = 0;
        for (Parameter parameter : parameters) {
            BODY body = parameter.getAnnotation(BODY.class);
            if (Objects.nonNull(body)) {
                bodyAnnotationCount++;
            }
        }
        if (bodyAnnotationCount > 1) {
            throw new RuntimeException(String.format("method: %s can not pined two @BODY annotation", method.getName()));
        }

        OK_METHOD_SET.add(method.toString());
    }
}
