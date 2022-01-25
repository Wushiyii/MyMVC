package com.wushiyii.controller;

import com.wushiyii.annotation.GET;
import com.wushiyii.annotation.Param;

/**
 * @Author: wgq
 * @Date: 2022/1/24 15:38
 */
public class TestController {

    @GET(path = "/hello")
    public String hello(@Param("name") String name, @Param("age") String age) {
        return "hi:" + name + ",age:" + age;
    }
}
