package com.wushiyii.handler;

import com.wushiyii.dto.Constants;
import com.wushiyii.handler.impl.DeleteRequestHandler;
import com.wushiyii.handler.impl.GetRequestHandler;
import com.wushiyii.handler.impl.PostRequestHandler;
import com.wushiyii.handler.impl.PutRequestHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wgq
 * @Date: 2022/1/25 11:47
 */
public class RequestHandlerFactory {

    // 比较古早的初始化方式 但是很好用
    private static final Map<String, RequestHandler> HANDLER_MAP = new HashMap<String, RequestHandler>(){{
        put(Constants.GET, new GetRequestHandler());
        put(Constants.POST, new PostRequestHandler());
        put(Constants.PUT, new PostRequestHandler());
        put(Constants.DELETE, new PostRequestHandler());
    }};


    public static RequestHandler getHandler(String methodName) {
        if (!HANDLER_MAP.containsKey(methodName)) {
            throw new RuntimeException("Not support http method : " + methodName);
        }
        return HANDLER_MAP.get(methodName);
    }


}
