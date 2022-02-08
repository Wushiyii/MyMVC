package com.wushiyii.controller;

import com.wushiyii.annotation.BODY;
import com.wushiyii.annotation.GET;
import com.wushiyii.annotation.PARAM;
import com.wushiyii.annotation.POST;
import lombok.Data;

import java.io.File;

/**
 * @Author: wgq
 * @Date: 2022/2/8 15:09
 */
public class TestController {

    @GET({"hello", "hello2"})
    public String hello(@PARAM("name") String name, @PARAM("age") String age) {
        return "hi:" + name + ",age:" + age;
    }


    @POST("/print")
    public String print(@PARAM("name") String name, @PARAM("age") Integer age) {
        return "hi:" + name + ",age:" + age;
    }

    @POST("print2")
    public PrintReq print2(@BODY PrintReq req) {
        return req;
    }

    @POST("uploadFile")
    public String uploadFile(@PARAM("file") File file, @PARAM("hello") String hello) {
        return "hello:" + hello + ", file :" + file.getName();
    }

    @POST("uploadFile2/")
    public UploadReq uploadFile2(@BODY UploadReq req) {
        return req;
    }


    @Data
    public static class PrintReq {
        private String name;
        private Integer age;
        private EmbedPrintReq embed;
    }

    @Data
    public static class EmbedPrintReq {
        private String name;
        private Integer age;
        private EmbedPrintReq2 embed2;
    }

    @Data
    public static class EmbedPrintReq2 {
        private String name;
        private Integer age;
    }

    @Data
    public static class UploadReq {
        private File file;
        private String hello;
    }
}
