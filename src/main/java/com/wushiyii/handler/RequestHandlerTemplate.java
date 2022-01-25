package com.wushiyii.handler;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @Author: wgq
 * @Date: 2022/1/25 11:44
 */
@Slf4j
public class RequestHandlerTemplate {

    public static void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            //获取处理器
            RequestHandler handler = RequestHandlerFactory.getHandler(req.getMethod());

            //建立上线文
            RequestContext context = RequestContext.builder().req(req).resp(resp).build();

            //预处理
            handler.prepareContext(context);

            //解析参数
            context.setParamMap(handler.parseParam(context));

            //执行
            context.setOriginResult(handler.handleRequest(context));

            //处理结果
            handler.handleResponse(context);
        } catch (Exception e) {
            log.error("invoke method error, method={}", req.getMethod(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
