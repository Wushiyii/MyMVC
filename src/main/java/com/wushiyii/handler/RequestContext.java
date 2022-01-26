package com.wushiyii.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 请求上下文
 * @Author: wgq
 * @Date: 2022/1/25 13:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestContext {

    private HttpServletRequest req;

    private HttpServletResponse resp;

    private String method;

    private String path;

    private Map<String, Object> paramMap;

    private Object originResult;




}
