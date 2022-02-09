package com.wushiyii.handler;

import com.alibaba.fastjson.JSON;
import com.wushiyii.dispatch.EndpointExecutor;
import com.wushiyii.dispatch.EndpointManager;
import com.wushiyii.dispatch.EndpointMetaInfo;
import com.wushiyii.utils.ParamUtils;
import com.wushiyii.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: wgq
 * @Date: 2022/1/25 11:42
 */
public interface RequestHandler {

    /**
     * 预处理context
     * @param context context
     * @throws Exception ex
     */
    default void prepareContext(RequestContext context) throws Exception {
        HttpServletRequest req = context.getReq();
        req.setCharacterEncoding("UTF-8");

        String reqPath = StringUtils.trimSlash(req.getPathInfo());

        context.setMethod(req.getMethod());
        context.setPath(reqPath);
    }

    /**
     * 解析入参，默认parameterMap格式获取
     * @param context context
     */
    default Map<String, Object> parseParam(RequestContext context) throws Exception {
        return ParamUtils.handleParameterMap(context.getReq());
    }

    /**
     * 执行
     * @param context context
     * @return 业务返回值
     * @throws Exception ex
     */
    default Object handleRequest(RequestContext context) throws Exception {
        EndpointMetaInfo endpoint = EndpointManager.getEndpoint(context.getMethod(), context.getPath());

        return EndpointExecutor.invokeEndpoint(endpoint, context.getParamMap());
    }

    /**
     * 处理返回值, 默认JSON序列化
     * @param context context
     * @throws Exception ex
     */
    default void handleResponse(RequestContext context) throws Exception{
        HttpServletResponse resp = context.getResp();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (Objects.nonNull(context.getOriginResult())) {
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(context.getOriginResult()));
            writer.flush();
        }
    }

}
