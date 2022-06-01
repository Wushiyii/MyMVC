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
