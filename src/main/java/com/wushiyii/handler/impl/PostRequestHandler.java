package com.wushiyii.handler.impl;

import com.wushiyii.dto.Constants;
import com.wushiyii.handler.RequestContext;
import com.wushiyii.handler.RequestHandler;
import com.wushiyii.utils.ParamUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: wgq
 * @Date: 2022/1/25 11:49
 */
public class PostRequestHandler implements RequestHandler {



    @Override
    public Map<String, Object> parseParam(RequestContext context) throws Exception {

        HttpServletRequest req = context.getReq();

        if (Objects.isNull(req.getContentType()) || "".equals(req.getContentType())) {
            throw new RuntimeException("[MyMVC] content-type is empty: " + req);
        }

        //几种content type的取入参方式
        if (req.getContentType().contains(Constants.FORM_CONTENT_TYPE)) {
            return RequestHandler.super.parseParam(context);
        } else if (req.getContentType().contains(Constants.JSON_CONTENT_TYPE)) {
            return ParamUtils.handleJSON(req);
        } else if (req.getContentType().contains(Constants.MULTI_PART_FORM_CONTENT_TYPE)) {
            return ParamUtils.handleMultiPart(req);
        } else {
            throw new RuntimeException("[MyMVC] not support content-type=" + req.getContentType());
        }
    }

}
