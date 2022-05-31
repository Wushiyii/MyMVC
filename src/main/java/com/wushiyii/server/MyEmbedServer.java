package com.wushiyii.server;

import com.wushiyii.config.MyMVCConfiguration;
import com.wushiyii.servlet.MyMVCServlet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * @Author: wgq
 * @Date: 2022/1/24 14:04
 */
@Slf4j
public class MyEmbedServer implements EmbedServer {


    private final Tomcat tomcat;

    public MyEmbedServer(int port, String contextPath) {
        try {
            this.tomcat = new Tomcat();
            this.tomcat.setPort(port);

            File root = getRootFolder();
            File webContentFolder = new File(root.getAbsolutePath(), "src/main/resources/");
            if (!webContentFolder.exists()) {
                webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
            }

            StandardContext standardContext = (StandardContext) this.tomcat.addWebapp(contextPath, webContentFolder.getAbsolutePath());
            standardContext.setParentClassLoader(this.getClass().getClassLoader());

            WebResourceRoot webResourceRoot = new StandardRoot(standardContext);
            standardContext.setResources(webResourceRoot);

            this.tomcat.addServlet(contextPath, "myMVCServlet", new MyMVCServlet()).setLoadOnStartup(0);

            standardContext.addServletMappingDecoded("/*", "myMVCServlet");

        } catch (Exception e) {
            log.error("[MyMVC] Init Tomcat Server failed ", e);
            throw new RuntimeException(e);
        }


    }



    @SneakyThrows
    @Override
    public void start() {
        this.tomcat.start();
        log.info("[MyMVC] start success, address: http://{}:{}", InetAddress.getLocalHost().getHostAddress(), MyMVCConfiguration.getPort());
        this.tomcat.getServer().await();
    }

    @SneakyThrows
    @Override
    public void stop() {
        this.tomcat.stop();
    }



    private File getRootFolder() {
        try {
            File root;
            String runningJarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) {
                root = new File("");
            } else {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            log.info("Tomcat:application resolved root folder: [{}]", root.getAbsolutePath());
            return root;
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
