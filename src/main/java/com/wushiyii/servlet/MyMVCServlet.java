package com.wushiyii.servlet;

import com.alibaba.fastjson.JSON;
import com.wushiyii.annotation.DELETE;
import com.wushiyii.annotation.GET;
import com.wushiyii.annotation.POST;
import com.wushiyii.annotation.PUT;
import com.wushiyii.config.MyMVCConfiguration;
import com.wushiyii.dispatch.EndpointManager;
import com.wushiyii.dispatch.EndpointMetaInfo;
import com.wushiyii.utils.ClassUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: wgq
 * @Date: 2022/1/22 15:16
 */
@Slf4j
public class MyMVCServlet extends HttpServlet {


    @Override
    public void init(ServletConfig config) throws ServletException {

        //执行初始化

        //基本包路径
        String basePackage = MyMVCConfiguration.getBasePackage();
        log.info("base:" + basePackage);

        //扫描注解
        Set<Class<?>> allPackageClass = ClassUtil.getPackageClass(basePackage);
        for (Class<?> clazz : allPackageClass) {

            for (Method declaredMethod : clazz.getDeclaredMethods()) {

                if (declaredMethod.isAnnotationPresent(GET.class) || declaredMethod.isAnnotationPresent(POST.class) ||
                        declaredMethod.isAnnotationPresent(PUT.class) || declaredMethod.isAnnotationPresent(DELETE.class)) {

                    registerEndpoint(clazz, declaredMethod);
                }
            }
        }
    }


    @SneakyThrows
    private void registerEndpoint(Class<?> clazz, Method declaredMethod) {

        String[] multiPath = {};
        if (declaredMethod.isAnnotationPresent(GET.class)) {
            multiPath = declaredMethod.getAnnotation(GET.class).path();
        } else if (declaredMethod.isAnnotationPresent(POST.class)) {
            multiPath = declaredMethod.getAnnotation(POST.class).path();
        } else if (declaredMethod.isAnnotationPresent(DELETE.class)) {
            multiPath = declaredMethod.getAnnotation(DELETE.class).path();
        } else if (declaredMethod.isAnnotationPresent(PUT.class)) {
            multiPath = declaredMethod.getAnnotation(PUT.class).path();
        }

        for (String path : multiPath) {

            EndpointMetaInfo metaInfo = EndpointMetaInfo.builder()
                    .path(path)
                    .endpointObject(clazz.newInstance())
                    .method(declaredMethod)
                    .build();
            EndpointManager.register(path, metaInfo);
        }

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String reqMethod = req.getMethod();
        String reqPath = req.getPathInfo();

        if (reqPath.endsWith("/")) {
            reqPath = reqPath.substring(0, reqPath.length() - 1);
        }

        EndpointMetaInfo endpoint = EndpointManager.getEndpoint(reqPath);
        if (Objects.isNull(endpoint)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        invokeEndpoint(endpoint, req, resp);
    }

    @SneakyThrows
    private void invokeEndpoint(EndpointMetaInfo endpoint, HttpServletRequest req, HttpServletResponse resp) {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();

        try {
            Object result = endpoint.getMethod().invoke(endpoint.getEndpointObject());
            writer.write(JSON.toJSONString(result));
            writer.flush();
        } catch (Exception e) {
            log.error("invoke method error, method={}", endpoint.getMethod(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
