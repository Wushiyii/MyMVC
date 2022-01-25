package com.wushiyii.utils;

import com.wushiyii.dto.Constants;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: wgq
 * @Date: 2022/1/25 11:07
 */
public class ParamUtils {


    public static Map<String, String> getRequestMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();


//        if (Constants.GET.equals(request.getMethod())) {
//            return handleParameterMap(request);
//        }
//
        return handleParameterMap(request);
    }

    public static Map<String, String> handleParameterMap(HttpServletRequest request) {

        Map<String, String> paramMap = new HashMap<>();
        request.getParameterMap().forEach((paramName, paramsValues) -> {
            if (Objects.nonNull(paramsValues)) {
                paramMap.put(paramName, paramsValues[0]);
            }
        });
        return paramMap;

    }

}
