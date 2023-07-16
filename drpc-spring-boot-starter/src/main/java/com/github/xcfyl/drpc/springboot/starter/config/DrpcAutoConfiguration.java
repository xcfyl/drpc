package com.github.xcfyl.drpc.springboot.starter.config;

import com.github.xcfyl.drpc.core.client.DrpcClient;
import com.github.xcfyl.drpc.core.client.DrpcRemoteReference;
import com.github.xcfyl.drpc.core.filter.client.DrpcClientFilter;
import com.github.xcfyl.drpc.core.filter.server.DrpcServerFilter;
import com.github.xcfyl.drpc.core.server.DrpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 19:04
 */
@Configuration
public class DrpcAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DrpcAutoConfiguration.class);

    @Bean
    public DrpcClient drpcClient(List<DrpcClientFilter> drpcClientFilters) throws Exception {
        DrpcClient drpcClient = new DrpcClient();
        drpcClient.init();
        if (drpcClientFilters != null && !drpcClientFilters.isEmpty()) {
            for (DrpcClientFilter filter : drpcClientFilters) {
                drpcClient.addFilter(filter);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("add filters by annotation are {}", drpcClientFilters);
            }
        }
        return drpcClient;
    }

    @Bean
    public DrpcRemoteReference drpcRemoteReference(DrpcClient drpcClient) throws Exception {
        return drpcClient.getDrpcRemoteReference();
    }

    @Bean
    public DrpcServer drpcServer(List<DrpcServerFilter> drpcServerFilters) throws Exception {
        DrpcServer drpcServer = new DrpcServer();
        drpcServer.init();
        if (drpcServerFilters != null && !drpcServerFilters.isEmpty()) {
            for (DrpcServerFilter filter : drpcServerFilters) {
                drpcServer.addFilter(filter);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("add filters by annotation are {}", drpcServerFilters);
            }
        }
        return drpcServer;
    }
}
