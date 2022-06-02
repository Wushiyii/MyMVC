# MyMVC

MyMVC ———— 基于GenericServlet、内嵌Tomcat实现的简单MVC框架

### 特性
- 内嵌Tomcat,不用另外操作各种容器
- 支持常见GET/POST/PUT/DELETE等协议
- 支持上传文件
- 一行代码即可启动,代码即配置
- 源代码总行数830行，非常轻量易懂

### TODO
- Path Variable
- Undertow/Jetty 等容器选择
- SpringBoot Starter支持

### Example
启动：
```java
public class AppTest{

    @Test
    public void start() {
        MyMVCBuilder.builder().port(7400).contextPath("/mvc").start(AppTest.class);
    }
}
```

Controller：
```java
@MyController("test")
public class TestController {

    @GET({"hello", "hello2"})
    public String hello(@PARAM("name") String name, @PARAM("age") String age) {
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
}
```

### 实现细节
主要分为三步，启动Tomcat、暴露web服务、处理请求

- 构造并启动`Tomcat`
```java
public MyEmbedServer(int port, String contextPath) {
    try {
        this.tomcat = new Tomcat();
        this.tomcat.setPort(port);
        File root = getRootFolder();
        File webContentFolder = new File(root.getAbsolutePath(), "src/main/resources/");
        if (!webContentFolder.exists()) {
        webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
    }
        
    //构造context
    StandardContext standardContext = (StandardContext) this.tomcat.addWebapp(contextPath, webContentFolder.getAbsolutePath());
    standardContext.setParentClassLoader(this.getClass().getClassLoader());

    //设置webRoot
    WebResourceRoot webResourceRoot = new StandardRoot(standardContext);
    standardContext.setResources(webResourceRoot);

    //增加统一Servlet入口
    this.tomcat.addServlet(contextPath, "myMVCServlet", new MyMVCServlet()).setLoadOnStartup(0);

    //所有请求入口走同一个Servlet
    standardContext.addServletMappingDecoded("/*", "myMVCServlet");

    } catch (Exception e) {
        log.error("[MyMVC] Init Tomcat Server failed ", e);
        throw new RuntimeException(e);
    }
}



@SneakyThrows
@Override
public void start() {
    //启动，阻塞等待请求
    this.tomcat.start();
    log.info("[MyMVC] start success, address: http://{}:{}", InetAddress.getLocalHost().getHostAddress(), MyMVCConfiguration.getPort());
    this.tomcat.getServer().await();
} 
```

- 暴露web服务
```java
@Slf4j
public class MyMVCServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {

        //基本包路径
        String basePackage = MyMVCConfiguration.getBasePackage();
        log.info("MyMVC init basePackage:" + basePackage);

        //扫描注解
        Set<Class<?>> allPackageClass = ClassUtil.getPackageClass(basePackage);
        for (Class<?> clazz : allPackageClass) {
            //扫描所有@MyController注解下的@Get/@POST/@PUT/@DELETE方法
            if (clazz.isAnnotationPresent(MyController.class)) {
                MyController myController = clazz.getAnnotation(MyController.class);
                for (Method declaredMethod : clazz.getDeclaredMethods()) {

                    if (declaredMethod.isAnnotationPresent(GET.class) || declaredMethod.isAnnotationPresent(POST.class) ||
                            declaredMethod.isAnnotationPresent(PUT.class) || declaredMethod.isAnnotationPresent(DELETE.class)) {
                        //注册执行器端点
                        EndpointManager.register(clazz, myController.value(), declaredMethod);
                    }
                }
            }

        }
    }

    //统一处理请求入口
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestHandlerTemplate.handle(req, resp);
    }
} 

```
- 处理请求

```java
@Slf4j
public class RequestHandlerTemplate {

    public static void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            //获取处理器
            RequestHandler handler = RequestHandlerFactory.getHandler(req.getMethod());

            //建立上下文
            RequestContext context = RequestContext.builder().req(req).resp(resp).build();

            //预处理
            handler.prepareContext(context);

            //解析参数
            context.setParamMap(handler.parseParam(context));

            //执行
            context.setOriginResult(handler.handleRequest(context));

            //处理结果
            handler.handleResponse(context);
        } catch (Exception e) {
            log.error("invoke method error, method={}", req.getMethod(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
```