package com.wushiyii.controller;

import com.wushiyii.annotation.GET;
import com.wushiyii.annotation.POST;
import com.wushiyii.annotation.Param;

/**
 * @Author: wgq
 * @Date: 2022/1/24 15:38
 */
public class TestController {

    @GET(path = {"/hello", "/hello2"})
    public String hello(@Param("name") String name, @Param("age") String age) {
        return "hi:" + name + ",age:" + age;
    }


    @POST(path = {"/print"})
    public String print(@Param("name") String name, @Param("age") String age) {
        return "hi:" + name + ",age:" + age;
    }
}
