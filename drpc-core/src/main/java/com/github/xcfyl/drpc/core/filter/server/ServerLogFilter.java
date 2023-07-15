package com.github.xcfyl.drpc.core.filter.server;

import com.github.xcfyl.drpc.core.protocol.RpcRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:14
 */
@Slf4j
public class ServerLogFilter implements ServerFilter {
    @Override
    public void filter(ServerFilterChain chain, RpcRequest request) {
        log.debug("receive request #{}", request);
    }
}
