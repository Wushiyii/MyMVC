package com.wushiyii.handler;

import com.alibaba.fastjson.JSON;
import com.wushiyii.utils.ParamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

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

        String reqPath = req.getPathInfo();
        if (reqPath.endsWith("/")) {
            reqPath = reqPath.substring(0, reqPath.length() - 1);
        }

        context.setMethod(req.getMethod());
        context.setPath(reqPath);
    }

    /**
     * 解析入参，默认parameterMap格式获取
     * @param context context
     */
    default Map<String, String > parseParam(RequestContext context) {
        return ParamUtils.handleParameterMap(context.getReq());
    }

    /**
     * 执行
     * @param context context
     * @return 业务返回值
     * @throws Exception ex
     */
    Object handleRequest(RequestContext context) throws Exception;

    /**
     * 处理返回值, 默认JSON序列化
     * @param context context
     * @throws Exception ex
     */
    default void handleResponse(RequestContext context) throws Exception{
        HttpServletResponse resp = context.getResp();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();
        writer.write(JSON.toJSONString(context.getOriginResult()));
        writer.flush();
    }

}
