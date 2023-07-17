package com.github.xcfyl.drpc.core.filter.client;

import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;
import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:13
 */
public class DrpcClientLogFilter extends DrpcAbstractClientFilter {
    private static final Logger logger = LoggerFactory.getLogger(DrpcClientLogFilter.class);

    @Override
    public void filter(DrpcClientFilterChain chain, List<DrpcConnectionWrapper> connectionWrappers, DrpcRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("send request, requestId -> {}, serviceName -> {}, method -> {}",
                    request.getId(), request.getServiceName(), request.getMethodName());
        }
        chain.doFilter(connectionWrappers, request);
    }
}
