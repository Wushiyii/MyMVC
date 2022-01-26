package com.wushiyii.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: wgq
 * @Date: 2022/1/25 11:07
 */
public class ParamUtils {

    //JSON转MSS 静态变量
    private static final TypeReference<Map<String, Object>> MSS_TYPE_REFERENCE =  new TypeReference<Map<String, Object>>(){};

    // 上传文件相关配置
    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    private static final ServletFileUpload uploader =
            new ServletFileUpload(new DiskFileItemFactory(MEMORY_THRESHOLD, new File(System.getProperty("java.io.tmpdir"))));

    static {
        // 设置最大文件上传值
        uploader.setFileSizeMax(MAX_FILE_SIZE);
        // 设置最大请求值 (包含文件和表单数据)
        uploader.setSizeMax(MAX_REQUEST_SIZE);
        // 中文处理
        uploader.setHeaderEncoding("UTF-8");
    }



    /**
     * 处理parameterMap参数
     * @param request req
     * @return map
     */
    public static Map<String, Object> handleParameterMap(HttpServletRequest request) {

        Map<String, Object> paramMap = new HashMap<>();
        request.getParameterMap().forEach((paramName, paramsValues) -> {
            if (Objects.nonNull(paramsValues)) {
                paramMap.put(paramName, paramsValues[0]);
            }
        });
        return paramMap;

    }

    /**
     * 解析JSON
     * @param req req
     * @return map
     * @throws IOException ex
     */
    public static Map<String, Object> handleJSON(HttpServletRequest req) throws IOException {

        // JSON 单行解析
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        //将json字符串转换为json对象
        return JSONObject.parseObject(sb.toString(), MSS_TYPE_REFERENCE);
    }


    /**
     * 解析文件
     * @param request req
     * @return map
     */
    public static Map<String, Object> handleMultiPart(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();

        ServletRequestContext servletRequestContext = new ServletRequestContext(request);
        String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;

        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            List<FileItem> formItems = uploader.parseRequest(servletRequestContext);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // 在控制台输出文件的上传路径
                        paramMap.put(item.getFieldName(), storeFile);
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("upload file error ", ex);
        }
        return paramMap;

    }
}
