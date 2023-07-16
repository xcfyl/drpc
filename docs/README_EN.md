[TOC]



# 一、README.md

+ [中文](../README.md)

# 二、Introduction

This project is a simple rpc system built with modular design ideas, suitable for learning rpc principles and mastering basic rpc concepts. The main contents are as follows:

1. rpc communication protocol format based on netty
2. Zookeeper-based registry implementation
3. Jdk-based dynamic proxy
4. jdk and FastJSON-based serialization
5. Event publishing mechanism based on observer pattern
6. Chain of Responsibility based rpc client and server filters
7. Design and implementation of rpc routing layer, including random routing strategy and polling routing strategy
8. Easy integration with springboot for easy annotation development

# 三、Quick Start

## 3.1 Importing maven dependencies

```xml
<dependency>
    <groupId>com.github.xcfyl.drpc</groupId>
    <artifactId>drpc-spring-boot-starter</artifactId>
    <version>1.1-SNAPSHOT</version>
</dependency>
```

## 3.2 API Definition

```java
@DrpcReference
public interface ReplyService {
    String reply(String message);
}
```

## 3.3 API Implementation Definition

```java
@DrpcService
public class ReplyServiceImpl implements ReplyService {
    @Override
    public String reply(String message) {
        return message;
    }
}
```

## 3.4 Enabling drpc

### 3.4.1 Service Provider

1. Enabling Service Provider

```java
@EnableDrpc(scanPackages = "com.github.xcfyl.drpc")
@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
```

2. The service provider configuration template

``` properties
server.port=17001
server.request.limit=1024
server.registry.type=zookeeper
server.registry.addr=127.0.0.1:2181
server.application.name=app2
server.serializer=jdk
```

### 3.4.2 Service Consumer

1. Enabling Service Consumer

```java
@SpringBootApplication
@EnableDrpc(scanPackages = "com.github.xcfyl.drpc")
public class ConsumerApplication {
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
```

2. The service consumer configuration template

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

### 3.4.3 controller

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

