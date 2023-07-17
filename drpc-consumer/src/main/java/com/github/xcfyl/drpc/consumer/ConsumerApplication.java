package com.github.xcfyl.drpc.consumer;

import com.github.xcfyl.drpc.springboot.starter.annotation.EnableDrpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 14:34
 */
@EnableDrpcClient(scanPackages = "com.github.xcfyl.drpc")
@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
