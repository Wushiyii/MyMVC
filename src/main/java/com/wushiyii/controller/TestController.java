package com.wushiyii.controller;

import com.wushiyii.annotation.BODY;
import com.wushiyii.annotation.GET;
import com.wushiyii.annotation.POST;
import com.wushiyii.annotation.PARAM;
import lombok.Data;

import java.io.File;

/**
 * @Author: wgq
 * @Date: 2022/1/24 15:38
 */
public class TestController {

    @GET({"/hello", "/hello2"})
    public String hello(@PARAM("name") String name, @PARAM("age") String age) {
        return "hi:" + name + ",age:" + age;
    }


    @POST("/print")
    public String print(@PARAM("name") String name, @PARAM("age") String age) {
        return "hi:" + name + ",age:" + age;
    }

    @POST("/print2")
    public String print2(@BODY PrintReq req) {
        return "hi:" + req.getName() + ",age:" + req.getAge();
    }

    @POST("/uploadFile")
    public String uploadFile(@PARAM("file") File file, @PARAM("hello") String hello) {
        return "hello:" + hello + ", file :" + file.getName();
    }

    @POST("/uploadFile2")
    public String uploadFile2(@BODY UploadReq req) {
        return "hello:" + req.getHello() + ", file :" + req.getFile().getName();
    }


    @Data
    public static class PrintReq {
        private String name;
        private Integer age;
    }

    @Data
    public static class UploadReq {
        private File file;
        private String hello;
    }
}
