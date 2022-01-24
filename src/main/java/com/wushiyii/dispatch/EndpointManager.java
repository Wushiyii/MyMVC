package com.wushiyii.dispatch;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: wgq
 * @Date: 2022/1/22 15:20
 */
@Slf4j
public class EndpointManager {

    private final static Map<String, EndpointMetaInfo> endpointMap = new ConcurrentHashMap<>();

    public static void register(String path, EndpointMetaInfo endpointMetaInfo) {
        log.info("Register endpoint path={} metaInfo={}", path, endpointMetaInfo);
        endpointMap.put(path, endpointMetaInfo);
    }


    public static EndpointMetaInfo getEndpoint(String path) {
        return endpointMap.get(path);
    }


}
