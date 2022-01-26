package com.wushiyii.dispatch;

import com.wushiyii.annotation.Param;
import com.wushiyii.utils.ClassUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @Author: wgq
 * @Date: 2022/1/25 13:59
 */
@Slf4j
public class EndpointExecutor {


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

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> type = parameterTypes[i];

            Param pinedParam = param.getAnnotation(Param.class);

            if (requestMap.containsKey(pinedParam.value())) {
                objectList.add(ClassUtil.generateParameterObject(type, requestMap.get(pinedParam.value())));
            } else {
                objectList.add(ClassUtil.generateDefaultObject(type));
            }
        }


        return objectList;
    }
}
