package com.github.xcfyl.drpc.springboot.starter.config;

import com.github.xcfyl.drpc.core.filter.server.DrpcServerFilter;
import com.github.xcfyl.drpc.core.server.DrpcServer;
import com.github.xcfyl.drpc.springboot.starter.processor.DrpcServerImportSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/17 09:28
 */
@Configuration
public class DrpcServerAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DrpcServerAutoConfiguration.class);

    @ConditionalOnMissingBean(DrpcServer.class)
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
