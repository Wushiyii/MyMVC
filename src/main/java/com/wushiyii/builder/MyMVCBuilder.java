package com.wushiyii.builder;

import com.wushiyii.config.MyMVCConfiguration;
import com.wushiyii.server.MyEmbedServer;
import lombok.Getter;

/**
 * @Author: wgq
 * @Date: 2022/1/24 11:26
 */
@Getter
public class MyMVCBuilder {

    public static MyMVCBuilder of() {
        return new MyMVCBuilder();
    }

    public MyMVCBuilder port(int port) {
        MyMVCConfiguration.setPort(port);
        return this;
    }

    public MyMVCBuilder contextPath(String contextPath) {
        MyMVCConfiguration.setContextPath(contextPath);
        return this;
    }

    public void start(Class<?> startClass) {
        MyMVCConfiguration.setStartClass(startClass);
        new MyEmbedServer(MyMVCConfiguration.getPort(), MyMVCConfiguration.getContextPath()).start();
    }

}
