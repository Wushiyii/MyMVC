package com.wushiyii.controller;

import com.wushiyii.annotation.GET;

/**
 * @Author: wgq
 * @Date: 2022/1/24 15:38
 */
public class TestController {

    @GET(path = "/hello")
    public String hello() {
        return "hi:";
    }
}
