package com.github.xcfyl.drpc.springboot.starter.config;

import com.github.xcfyl.drpc.core.client.DrpcClient;
import com.github.xcfyl.drpc.core.client.DrpcRemoteReference;
import com.github.xcfyl.drpc.core.filter.client.DrpcClientFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/17 09:26
 */
@Configuration
public class DrpcClientAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DrpcClientAutoConfiguration.class);

    @ConditionalOnMissingBean(DrpcClient.class)
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

    @ConditionalOnMissingBean(DrpcRemoteReference.class)
    @Bean
    public DrpcRemoteReference drpcRemoteReference(DrpcClient drpcClient) throws Exception {
        return drpcClient.getDrpcRemoteReference();
    }
}
