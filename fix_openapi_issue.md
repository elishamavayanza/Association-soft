# 解决 springdoc-openapi 与 Spring Boot 3.5.4 版本不兼容问题

## 问题描述

在运行应用程序时遇到以下错误：

```
java.lang.NoSuchMethodError: 'void org.springframework.web.method.ControllerAdviceBean.<init>(java.lang.Object)'
        at org.springdoc.core.service.GenericResponseService.lambda$getGenericMapResponse$8(GenericResponseService.java:706) ~[springdoc-openapi-starter-common-2.6.0.jar:2.6.0]
```

## 原因分析

此错误是由于 Spring Boot 3.5.4 使用的是 Spring Framework 6.2，而 springdoc-openapi 2.6.0 版本还不完全兼容 Spring Framework 6.2 导致的。

在 Spring Framework 6.2 中，`ControllerAdviceBean` 类的构造函数发生了变化，从原来接受单个 Object 参数的构造函数改为需要更多参数的构造函数。

## 解决方案

将 springdoc-openapi 版本从 2.6.0 升级到 2.7.0，因为 2.7.0 版本已经解决了与 Spring Framework 6.2 的兼容性问题。

### 修改内容

在 pom.xml 文件中，找到 springdoc-openapi 依赖项并将其版本从 2.6.0 更新为 2.7.0：

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```

## 编译和测试

修改完成后，请执行以下操作：

1. 清理项目：`mvn clean`
2. 重新编译：`mvn compile`
3. 运行应用程序：`mvn spring-boot:run`

验证问题是否解决，访问 Swagger UI 页面（通常是 http://localhost:8089/swagger-ui.html）。