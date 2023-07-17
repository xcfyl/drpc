package com.github.xcfyl.drpc.core.filter.server;

import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:14
 */
public class DrpcServerLogFilter extends DrpcAbstractServerFilter {
    private static final Logger logger = LoggerFactory.getLogger(DrpcServerLogFilter.class);

    @Override
    public void filter(DrpcServerFilterChain chain, DrpcRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("receive request, requestId -> {}, serviceName -> {}, method0 -> {}",
                    request.getId(), request.getServiceName(), request.getMethodName());
        }
        chain.doFilter(request);
    }
}
