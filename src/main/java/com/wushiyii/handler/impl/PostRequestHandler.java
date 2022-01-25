package com.wushiyii.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wushiyii.dispatch.EndpointExecutor;
import com.wushiyii.dispatch.EndpointManager;
import com.wushiyii.dispatch.EndpointMetaInfo;
import com.wushiyii.dto.Constants;
import com.wushiyii.handler.RequestContext;
import com.wushiyii.handler.RequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author: wgq
 * @Date: 2022/1/25 11:49
 */
public class PostRequestHandler implements RequestHandler {

    private final TypeReference<Map<String, String>> MSS_TYPE_REFERENCE =  new TypeReference<Map<String, String>>(){};


    @Override
    public Map<String, String> parseParam(RequestContext context) throws Exception {

        HttpServletRequest req = context.getReq();

        //几种content type的取入参方式
        if (Constants.FORM_CONTENT_TYPE.equals(req.getContentType())) {
            return RequestHandler.super.parseParam(context);
        } else if (Constants.JSON_CONTENT_TYPE.equals(req.getContentType())) {
            // JSON 单行解析
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getReq().getInputStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            //将json字符串转换为json对象
            return JSONObject.parseObject(sb.toString(), MSS_TYPE_REFERENCE);
        } else if (Constants.MULTI_PART_FORM_CONTENT_TYPE.equals(req.getContentType())) {
            //TODO 上传文件
        } else {
            throw new RuntimeException("[MyMVC] not support content-type=" + req.getContentType());
        }

        return RequestHandler.super.parseParam(context);
    }

}
