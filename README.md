# 一、简介

该项目是采用模块化设计思路构建的简易rpc系统，适合rpc原理学习，掌握基本的rpc概念。主要包含如下内容：

1. 基于netty的rpc通信协议格式
2. 基于zookeeper的注册中心实现
3. 基于jdk的动态代理
4. 基于jdk和fastjson的序列化
5. 基于观察者模式的事件发布机制
6. 基于责任链模式的rpc客户端和服务端过滤器
7. rpc路由层设计和实现，主要包括随机路由策略和轮询路由策略
8. 和springboot的简易整合，支持简单的注解开发

# 二、基本上使用

## 2.1 引入maven依赖

```xml
<dependency>
    <groupId>com.github.xcfyl.drpc</groupId>
    <artifactId>drpc-spring-boot-starter</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```

## 2.2 api定义

```java
@DrpcReference
public interface ReplyService {
    String reply(String message);
}
```

## 2.3 api实现定义

```java
@DrpcService
public class ReplyServiceImpl implements ReplyService {
    @Override
    public String reply(String message) {
        return message;
    }
}
```

## 2.4 启用drpc

### 2.4.1 服务提供者

1. 服务提供者启用

```java
@EnableDrpc(scanPackages = "com.github.xcfyl.drpc")
@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
```

2. 服务提供者的配置文件

``` properties
server.port=17001
server.request.limit=1024
server.registry.type=zookeeper
server.registry.addr=127.0.0.1:2181
server.application.name=app2
server.serializer=jdk
```

### 2.4.2 服务消费者

1. 服务消费者启用

```java
@SpringBootApplication
@EnableDrpc(scanPackages = "com.github.xcfyl.drpc")
public class ConsumerApplication {
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
```

2. 服务消费者的配置文件

```properties
client.request.timeout=3000
client.proxy=jdk
client.router=roundrobin
client.request.limit=2048
client.registry.type=zookeeper
client.registry.addr=127.0.0.1:2181
client.application.name=client1
client.serializer=jdk
client.subscribe.retry.times=3
client.subscribe.retry.interval=1000
```

### 2.4.3 controller

```java
@RestController
@ResponseBody
public class ReplyController {
    @Resource
    private ReplyService replyService;

    @GetMapping("/reply")
    public String reply(@RequestParam("msg") String message) {
        return replyService.reply(message);
    }
}
```

