package com.github.xcfyl.drpc.springboot.starter.config;

import com.github.xcfyl.drpc.core.client.DrpcClient;
import com.github.xcfyl.drpc.core.client.DrpcRemoteReference;
import com.github.xcfyl.drpc.core.server.DrpcServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 19:04
 */
@Configuration
public class DrpcAutoConfiguration {
    @Bean
    public DrpcClient drpcClient() {
        return new DrpcClient();
    }

    @Bean
    public DrpcServer drpcServer() throws Exception {
        DrpcServer drpcServer = new DrpcServer();
        drpcServer.init();
        return drpcServer;
    }
}
