package com.wushiyii.handler.impl;

import com.wushiyii.dispatch.EndpointExecutor;
import com.wushiyii.dispatch.EndpointManager;
import com.wushiyii.dispatch.EndpointMetaInfo;
import com.wushiyii.handler.RequestContext;
import com.wushiyii.handler.RequestHandler;
/**
 * @Author: wgq
 * @Date: 2022/1/25 11:48
 */
public class GetRequestHandler implements RequestHandler {


    @Override
    public Object handleRequest(RequestContext context) throws Exception {

        EndpointMetaInfo endpoint = EndpointManager.getEndpoint(context.getMethod(), context.getPath());

        return EndpointExecutor.invokeEndpoint(endpoint, context.getParamMap());
    }
}
