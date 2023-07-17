package com.github.xcfyl.drpc.provider;

import com.github.xcfyl.drpc.springboot.starter.annotation.EnableDrpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 18:45
 */
@EnableDrpcServer(scanPackages = "com.github.xcfyl.drpc")
@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
