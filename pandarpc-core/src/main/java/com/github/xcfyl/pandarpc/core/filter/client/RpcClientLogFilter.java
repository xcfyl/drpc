package com.github.xcfyl.pandarpc.core.filter.client;

import com.github.xcfyl.pandarpc.core.client.ConnectionWrapper;
import com.github.xcfyl.pandarpc.core.protocol.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:13
 */
@Slf4j
public class RpcClientLogFilter implements RpcClientFilter {
    @Override
    public void filter(RpcClientFilterChain chain, List<ConnectionWrapper> connectionWrappers, RpcRequest request) {
        log.debug("send request #{}", request);
    }
}
