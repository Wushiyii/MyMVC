package com.wushiyii.servlet;

import com.alibaba.fastjson.JSON;
import com.wushiyii.annotation.*;
import com.wushiyii.config.MyMVCConfiguration;
import com.wushiyii.dispatch.EndpointManager;
import com.wushiyii.handler.RequestHandlerTemplate;
import com.wushiyii.utils.ClassUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

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
        log.info("MyMVC init basePackage:" + basePackage);

        //扫描注解
        Set<Class<?>> allPackageClass = ClassUtil.getPackageClass(basePackage);
        for (Class<?> clazz : allPackageClass) {

            for (Method declaredMethod : clazz.getDeclaredMethods()) {

                if (declaredMethod.isAnnotationPresent(GET.class) || declaredMethod.isAnnotationPresent(POST.class) ||
                        declaredMethod.isAnnotationPresent(PUT.class) || declaredMethod.isAnnotationPresent(DELETE.class)) {

                    //注册执行器端点
                    EndpointManager.register(clazz, declaredMethod);
                }
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestHandlerTemplate.handle(req, resp);
    }
}
